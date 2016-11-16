package com.example.kushagra.meetupapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kushagra.meetupapp.network.api.ServerApi;
import com.example.kushagra.meetupapp.network.model.StudentQueryClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        //tags = (MultiAutoCompleteTextView) this.findViewById(R.id.tags);
        ta = (AutoCompleteTextView) this.findViewById(R.id.ta);

        title=(EditText)findViewById(R.id.title);
        description=(EditText)findViewById(R.id.description);

        totime = (TextView) findViewById(R.id.totime);
        fromtime = (TextView) findViewById(R.id.fromtime);
        date = (TextView) findViewById(R.id.date);
        check = (CheckBox) findViewById(R.id.check);
        ll = (LinearLayout) findViewById(R.id.ll);
        mContext = getApplicationContext();

        String course = "cn";
        String[] suggestion=new String[0] ;

        tas = new String[2];
        tas[0]="pranks@iiitd.ac.in";
        tas[1]="kushagra.mahajan27@gmail.com";

        ArrayAdapter<String> ta_adapter = new ArrayAdapter<>(mContext,android.R.layout.simple_dropdown_item_1line,tas);
        ta.setAdapter(ta_adapter);


    }

    public void clickSendQuery(View v) throws IOException, ClassNotFoundException {



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AllCoursesActivity.IP_ADD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServerApi service = retrofit.create(ServerApi.class);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(AllCoursesActivity.SHARED_PREF_FILE_NAME,
                AllCoursesActivity.MODE_PRIVATE);
        String studentId = sharedPreferences.getString(AllCoursesActivity.EMAIL_ID_EXTRA, "");

        String courseId = getIntent().getStringExtra( AllCoursesActivity.COURSE_ID_EXTRA );

        EditText editText = (EditText)findViewById(R.id.title);
        EditText editTextDesp = (EditText)findViewById(R.id.description);
        final EditText editTextTA = (EditText)findViewById(R.id.ta);


        Log.d(MainActivity.TAG , "editext" + "ccc" );


        StudentQueryClass studentQueryClass = new StudentQueryClass(
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
                Log.d(MainActivity.TAG ," Query done Response");
                if(response.body()!=null)
                {
                    Query qadd=new Query(response.body().getQueryId(),response.body().getTitle(),response.body().getDescription(),editTextTA.getText().toString(),null);


                    String file_name=getIntent().getStringExtra(AllCoursesActivity.COURSE_ID_EXTRA );

                    file = new File(getApplicationContext().getFilesDir(),file_name);

//                    FileOutputStream fileOut=null;
//                    try {
//                        fileOut=new FileOutputStream(new File(getApplicationContext().getFilesDir(),file_name));
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//
//                    FileInputStream fileIn = null;// Read serial file.
//                    try {
//                        fileIn = new FileInputStream(new File(getApplicationContext().getFilesDir(),file_name));
//                        fileOut= new FileOutputStream(new File(getApplicationContext().getFilesDir(),file_name));
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
                    ObjectInputStream ois = null;

                    try
                    {
                        ois = new ObjectInputStream(new FileInputStream(file));// input the read file.
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    ArrayList<Query> Querarr = new ArrayList<Query>();

//                    try
//                    {
//                        Querarr= (ArrayList<Query>) in.readObject();
//                        Querarr.add(qadd);
//                        out.writeObject(Querarr);
//
//                    }
//                    catch (ClassNotFoundException e)
//                    {
//                        e.printStackTrace();
//                    }
//                    catch (IOException e)
//                    {
//                        e.printStackTrace();
//                    }

                    try {
                        Querarr = (ArrayList<Query>) ois.readObject() ;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        ois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ObjectOutputStream oos = null;

                    try {
                        oos = new ObjectOutputStream(new FileOutputStream(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        oos.writeObject(Querarr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//        fileIn = new FileInputStream(new File(this.getFilesDir(),file_name));// Read serial file.
//        in = null;
//        in = new ObjectInputStream(fileIn);
//        Querarr= (ArrayList<Query>) in.readObject();
//        System.out.println(Querarr.get(0).getTitle());

                    try {
                        oos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                else
                {
                    Log.d(MainActivity.TAG,"null respons on sending qeury");
                }


            }

            @Override
            public void onFailure(Call<StudentQueryClass> call, Throwable t) {

                Log.d(MainActivity.TAG , "failed query");
            }
        });
        finish();
    }

    public void clickToTime(View v)
    {
        final Calendar c = Calendar.getInstance();
        tHour = c.get(Calendar.HOUR_OF_DAY);
        tMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        totime.setText(hourOfDay + ":" + minute);
                    }
                }, tHour, tMinute, false);
        timePickerDialog.show();
    }
    public void clickFromTime(View v)
    {
        final Calendar c = Calendar.getInstance();
        fHour = c.get(Calendar.HOUR_OF_DAY);
        fMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        fromtime.setText(hourOfDay + ":" + minute);
                    }
                }, fHour, fMinute, false);
        timePickerDialog.show();
    }
    public void clickDate(View v)
    {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, year, month, day);
        datePickerDialog.show();
    }
    public void clickCheck(View v)
    {
        if(check.isChecked())
        {
            int h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
            ViewGroup.LayoutParams params = ll.getLayoutParams();
            params.height = h;
            ll.setLayoutParams(params);
        }
        else
        {
            ViewGroup.LayoutParams params = ll.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            ll.setLayoutParams(params);
        }
    }
}
