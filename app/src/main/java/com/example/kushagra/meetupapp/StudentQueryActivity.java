package com.example.kushagra.meetupapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class StudentQueryActivity extends AppCompatActivity {

    Context mContext;
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_query);

        mContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, StartNewStudentQueryActivity.class);

                Log.d(MainActivity.TAG , "Name" + getIntent().getStringExtra(AllCoursesActivity.COURSE_NAME_EXTRA)
                    + "Id" + getIntent().getStringExtra(AllCoursesActivity.COURSE_ID_EXTRA ));
                i.putExtra(AllCoursesActivity.COURSE_NAME_EXTRA , getIntent().getStringExtra(AllCoursesActivity.COURSE_NAME_EXTRA));
                i.putExtra(AllCoursesActivity.COURSE_ID_EXTRA , getIntent().getStringExtra(AllCoursesActivity.COURSE_ID_EXTRA ));

                startActivity(i);
            }
        });


        //get all the queries
        list = (RecyclerView) findViewById(R.id.list);

        ArrayList<Query> myQueries = new ArrayList<>();
        String file_name=getIntent().getStringExtra(AllCoursesActivity.COURSE_ID_EXTRA);

        FileInputStream fileIn = null;// Read serial file.
        try {
            fileIn = new FileInputStream(new File(this.getFilesDir(),file_name));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream in = null;// input the read file.
        try
        {
            in = new ObjectInputStream(fileIn);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        ArrayList<Query> Querarr = null;
        Log.d(MainActivity.TAG, (in==null) + " stats");
        /*try
        {
            if (in == null || in.readObject() == null || (ArrayList<Query>) in.readObject() == null)
            {
                Log.d(MainActivity.TAG ," readObj"+  in.readObject() );

                myQueries = new ArrayList<>();
            }
            else
            {
                Querarr = (ArrayList<Query>) in.readObject();// allocate it receiver the object file already instanciated.
                Log.d(MainActivity.TAG , Querarr.toString() + " "  + "Querr");
                myQueries = (ArrayList<Query>)Querarr.clone();

            }
        }*/
        try
        {
            fileIn = new FileInputStream(new File(this.getFilesDir(),file_name));// Read serial file.
            in = null;
            in = new ObjectInputStream(fileIn);
            myQueries = (ArrayList<Query>) in.readObject();
            Log.d( MainActivity.TAG ,"TITLER" + myQueries.get(0).getTitle());

        }
        catch (EOFException e )
        {
            Log.d(MainActivity.TAG  , " MALA ");
            e.printStackTrace();
            Log.d(MainActivity.TAG , "exception inside Querr");
            myQueries = Querarr;

        }
        catch (Exception fe)
        {
            fe.printStackTrace();
        }
//        myQueries.add(new Query("Ttile1","cse101",new ArrayList<Message>()));
//        myQueries.add(new Query("MAD","cse201",new ArrayList<Message>()));
//        myQueries.add(new Query("MC","cse301",new ArrayList<Message>()));

        String currentCourseId  =  getIntent().getStringExtra(AllCoursesActivity.COURSE_ID_EXTRA );

        QueryAdapter adapter = new QueryAdapter(myQueries , currentCourseId );

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager( mContext ));
    }

}
