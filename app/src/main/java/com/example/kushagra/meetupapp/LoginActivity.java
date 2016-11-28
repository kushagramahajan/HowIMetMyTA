package com.example.kushagra.meetupapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kushagra.meetupapp.db.DbManipulate;
import com.example.kushagra.meetupapp.db.objects.Course;
import com.example.kushagra.meetupapp.navDrawer.CommonCoursesListActivity;
import com.example.kushagra.meetupapp.network.api.ServerApi;
import com.example.kushagra.meetupapp.network.model.StudentRegisterClass;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity
{
    private Button mLoginSubmit;
    private Spinner spinner;
    private EditText mEmail;
    String email_text;
    String name="";
    StudentRegisterClass stobj;
    String currentCourse;
    LinearLayout table;
   // ArrayList<Course> myCourses;
    TextView email;
    ArrayList<Course> a;
    ArrayAdapter<String> spinnerAdapter;
    ArrayList<String> serverCourses;

    CheckBox[] cb;
    private DbManipulate dbManipulate;


    private ArrayList<Course> myCourses = new ArrayList<>();

    private static final String TAG ="SP_Main";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.back);
        dbManipulate = new DbManipulate(getApplicationContext());


        mEmail=(EditText)findViewById(R.id.email_edittext);
        spinner=(Spinner) findViewById(R.id.spinner);
        table = (LinearLayout)findViewById(R.id.table);
   //     myCourses = new ArrayList<>();

        serverCourses = new ArrayList<String>();


        a = dbManipulate.getAllCourses();

        for (Course c : a)
        {
            serverCourses.add(c.getCourseName());
        }

        email = (TextView) findViewById(R.id.email);
        SharedPreferences sh=getApplicationContext().getSharedPreferences(AllCoursesActivity.SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        email.setText(sh.getString(AllCoursesActivity.EMAIL_ID_EXTRA,"user"));

        int i=0;
        cb = new CheckBox[serverCourses.size()];
        for(String s : serverCourses)
        {
            cb[i] = new CheckBox(this);
            cb[i].setText(s);
            cb[i].setChecked(false);
            cb[i].setTextSize(20);
            table.addView(cb[i]);
            i++;
        }
    }
/*
    public void addToTable(View view)
    {

        currentCourse = spinner.getSelectedItem().toString();
        TextView t = new TextView(this);
        t.setText(currentCourse);
        table.addView(t);
        serverCourses.remove(serverCourses.indexOf(currentCourse));
        if(serverCourses.isEmpty() || serverCourses.size()==0)
        {
            view.setEnabled(false);
        }
        spinnerAdapter.notifyDataSetChanged();
        for(Course c : a)
        {
            if(c.getCourseName().equals(currentCourse))
            {
                myCourses.add(c);
                break;
            }
        }
    }

  */

    public void clickAdd(View view)
    {

        stobj = new StudentRegisterClass("" , "");

        for(CheckBox ch : cb)
        {
            if(ch.isChecked())
            {
                currentCourse = ch.getText().toString();
                serverCourses.remove(serverCourses.indexOf(currentCourse));
                for(Course c : a)
                {
                    if(c.getCourseName().equals(currentCourse))
                    {
                        myCourses.add(c);
                        break;
                    }
                }
            }
        }

        SharedPreferences sh = getApplicationContext().
                getSharedPreferences(AllCoursesActivity.SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);

        Log.d(MainActivity.TAG , sh.getString(AllCoursesActivity.USER_NAME_EXTRA,"name") + " ");


        stobj.setStudentId( sh.getString(AllCoursesActivity.EMAIL_ID_EXTRA,"email"));
        stobj.setName( sh.getString(AllCoursesActivity.USER_NAME_EXTRA,"name"));


        String[] reqCourseIDArray = new String[myCourses.size()];

        for(int i = 0 ; i< reqCourseIDArray.length ; i++)
        {
            reqCourseIDArray[i] = myCourses.get(i).getCourseId();

        }

        stobj.setMyStudentCourses( reqCourseIDArray  );


        ;

        for(Course c : myCourses)
        {
            Log.d(MainActivity.TAG , c.getCourseName() + c.getCourseId());
            File file = new File(getApplicationContext().getFilesDir(),c.getCourseId());
            try {
                file.createNewFile();
                System.out.println("FILEPATH LA : "+file.getAbsoluteFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        ArrayList<Course> dummyCourse = new ArrayList<>();


        for(Course ctemp : myCourses)
        {
            dummyCourse.add( new Course(ctemp.getCourseId() , ctemp.getCourseName() , null));

        }








//check the ips
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(AllCoursesActivity.IP_ADD)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ServerApi service = retrofit.create(ServerApi.class);

                Call<StudentRegisterClass> call = service.registerStudentOnServer(stobj);

                call.enqueue(new Callback<StudentRegisterClass>()
                {
                    @Override
                    public void onResponse(Call<StudentRegisterClass> call, Response<StudentRegisterClass> response)
                    {
                        if(response!=null)
                        {
                            StudentRegisterClass studentRegisterClass = response.body();
                            String[] reqArray = studentRegisterClass.getMyTaCourses();

                            ArrayList<Course> myTaCourses = new ArrayList<Course>();

                            Log.d(MainActivity.TAG , "Inner part" + response.body() );

                            for(int i=0 ; i < reqArray.length ;i++)
                            {
                                String cId = reqArray[i].split(";")[0];
                                String cName = reqArray[i].split(";")[1];
                                Course cTemp = new Course(cId, cName, null);
                                myTaCourses.add(cTemp);
                            }

                            if(myTaCourses.size() > 0)
                            {
                            Log.d(MainActivity.TAG , myTaCourses.get(0).getCourseName() + "as a TA");
                            dbManipulate.insertTASideMyCourses(myTaCourses);
                            }
                            dbManipulate.insertMyCourses(myCourses);


                        }
                        else
                        {
                            ///toast
                            Log.d(TAG,"No response after logging student");
                        }


                    }

                    @Override
                    public void onFailure(Call<StudentRegisterClass> call, Throwable t)
                    {


                    }




                });



        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences( AllCoursesActivity.SHARED_PREF_FILE_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(AllCoursesActivity.IS_LOGGED_IN_EXTRA , true);

        Log.d(MainActivity.TAG,  AllCoursesActivity.IS_LOGGED_IN_EXTRA + "true");

        editor.apply();


        //
        Intent i = new Intent( LoginActivity.this , CommonCoursesListActivity.class);
        startActivity(i);

        finish();

    }
}



