package com.example.kushagra.meetupapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kushagra.meetupapp.db.DbManipulate;
import com.example.kushagra.meetupapp.navDrawer.recyclerView.CommonCoursesAdapter;
import com.example.kushagra.meetupapp.navDrawer.recyclerView.CommonQueryAdapter;
import com.example.kushagra.meetupapp.network.api.ServerApi;
import com.example.kushagra.meetupapp.network.model.StudentQueryClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.kushagra.meetupapp.navDrawer.CommonCoursesListActivity.commonQueryList;
import static com.example.kushagra.meetupapp.navDrawer.CommonCoursesListActivity.mAdapter;

public class StartNewStudentQueryActivity extends AppCompatActivity
{

    AutoCompleteTextView tags,ta;
    TextView totime, fromtime, date;
    EditText title,description;
    CheckBox check;
    LinearLayout ll;
    int year,month,day,fHour,fMinute,tHour, tMinute;
    public String[] tas;
    Context mContext;
    File file;
    DbManipulate dbman;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray("tas",tas);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        tas=savedInstanceState.getStringArray("tas");

    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_new_query);
        dbman=new DbManipulate(getApplicationContext());
        //tags = (MultiAutoCompleteTextView) this.findViewById(R.id.tags);
        ta = (AutoCompleteTextView) this.findViewById(R.id.ta);

        title=(EditText)findViewById(R.id.title);
        description=(EditText)findViewById(R.id.description);
        mContext = getApplicationContext();

        String course = "cn";
        String[] suggestion=new String[0] ;

        tas = getTA();

        ArrayAdapter<String> ta_adapter = new ArrayAdapter<>(mContext,R.layout.dropdown,tas);
        ta.setAdapter(ta_adapter);


    }

    public void clickSendQuery(View v) throws IOException, ClassNotFoundException {



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.IP_ADD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServerApi service = retrofit.create(ServerApi.class);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF_FILE_NAME,
                Context.MODE_PRIVATE);
        String studentId = sharedPreferences.getString(Constants.EMAIL_ID_EXTRA, "");

        String courseId = getIntent().getStringExtra( Constants.COURSE_ID_EXTRA );

        EditText editText = (EditText)findViewById(R.id.title);
        EditText editTextDesp = (EditText)findViewById(R.id.description);
        final EditText editTextTA = (EditText)findViewById(R.id.ta);


        Log.d(Constants.TAG , "editext" + "ccc" );


        final StudentQueryClass studentQueryClass = new StudentQueryClass(
                editText.getText().toString(),
                editTextDesp.getText().toString(),
                studentId,
                courseId,
                true,
                editTextTA.getText().toString()
        );





        Call<StudentQueryClass> call = service.sendQuery(studentQueryClass);
        call.enqueue(new Callback<StudentQueryClass>() {
            @Override
            public void onResponse(Call<StudentQueryClass> call, Response<StudentQueryClass> response)
            {
                Log.d(Constants.TAG ," Query done Response");
                if(response.body()!=null)
                {
                    // new object
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                            Constants.SHARED_PREF_FILE_NAME , Context.MODE_PRIVATE
                    );

                    String student_email_id = sharedPreferences.getString(Constants.EMAIL_ID_EXTRA,"user");

                    //public Query(String qid, String title, String description, String taId, String studentId)
                    String courseId=getIntent().getStringExtra( Constants.COURSE_ID_EXTRA );
                    Query qadd = new
                            Query( response.body().getQueryId(),
                            studentQueryClass.getTitle(),
                            studentQueryClass.getDescription(),
                            editTextTA.getText().toString(),
                            student_email_id, courseId);

                    //file initialization
                    dbman.insertQueryCourse(response.body().queryId,courseId);

                    String file_name=getIntent().getStringExtra(Constants.COURSE_ID_EXTRA );
                    file = new File(getApplicationContext().getFilesDir(),file_name);



                    //read from old file
                    ArrayList<Query> Querarr = readQueryFile(file);

                    for (Query q : Querarr)
                    {
                        Log.d("FILE_FUNC" , q.getDescription());
                    }
                    //add new data
                    Querarr.add(qadd);

                    //write new data
                    writeQueryFile(file,Querarr);

                    try
                    {
                        ((CommonQueryAdapter) mAdapter).notifyDataSetChanged();
                        commonQueryList.clear();
                        commonQueryList.addAll(Querarr);
                    }
                    catch (Exception e)
                    {
                        ((CommonCoursesAdapter)mAdapter).notifyDataSetChanged();

                    }




                }
                else
                {
                    Log.d(Constants.TAG,"null respons on sending qeury");
                }


            }

            @Override
            public void onFailure(Call<StudentQueryClass> call, Throwable t) {

                Log.d(Constants.TAG , "failed description");
            }
        });


        finish();
    }

    ArrayList<Query> readQueryFile(File file)
    {
        ObjectInputStream ois = null;

        try
        {
            ois = new ObjectInputStream(new FileInputStream(file));// input the read file.
            Log.d("FILETAG","Object Input stream opened...");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Log.d("FILETAG","Object Input did not stream opened...");
        }

        ArrayList<Query> Querarr = new ArrayList<Query>();

        try {
            Querarr = (ArrayList<Query>) ois.readObject() ;
            Log.d("FILE_FUNC","File read of size "+Querarr.size());
        } catch (ClassNotFoundException e) {
            Log.d("FILE_FUNC","File read me Class not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("FILE_FUNC","File read me IO");
            e.printStackTrace();
        } catch (NullPointerException n)
        {
            n.printStackTrace();
            Log.d("FILE_FUNC","File read me null pointer");
        }

        try {
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException n)
        {
            n.printStackTrace();
        }
        return Querarr;
    }

    void writeQueryFile(File file, ArrayList<Query> Querarr)
    {
        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            Log.d("FILE_FUNC","File output stream opened");
        } catch (IOException e) {
            Log.d("FILE_FUNC","File output stream did not");
            e.printStackTrace();
        }

        try {
            oos.writeObject(Querarr);
            Log.d("FILE_FUNC","File written of size "+Querarr.size());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("FILE_FUNC","File was not written");
        }

        try {
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String[] getTA()
    {
        String[] str = {
                "psingh@iiitd.ac.in",
                "srishti13108@iiitd.ac.in",
                "abhishek13004@iiitd.ac.in",
                "naman13064@iiitd.ac.in",
                "deepikay@iiitd.ac.in",
                "arushi15008@iiitd.ac.in",
                "garima15014@iiitd.ac.in",
                "neha15040@iiitd.ac.in",
                "kapish15026@iiitd.ac.in",
                "arashdeep15007@iiitd.ac.in",
                "loveleen15029@iiitd.ac.in",
                "vijayita15063@iiitd.ac.in",
                "deepak15013@iiitd.ac.in",
                "hiya15020@iiitd.ac.in",
                "kanchanjot15023@iiitd.ac.in",
                "pranks@iiitd.ac.in",               //for evaluation
                "kushagra.mahajan27@gmail.com"      //for evaluation
        };

        return str;
    }

}
