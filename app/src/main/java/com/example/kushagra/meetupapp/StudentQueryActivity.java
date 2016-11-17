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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class StudentQueryActivity extends AppCompatActivity {

    Context mContext;
    RecyclerView list;
    QueryAdapter adapter;
    ArrayList<Query> Querarr;

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
                finish();
            }
        });

        list = (RecyclerView) findViewById(R.id.list);

        ArrayList<Query> myQueries = new ArrayList<>();
        String file_name=getIntent().getStringExtra(AllCoursesActivity.COURSE_ID_EXTRA);

        File file = new File(getApplicationContext().getFilesDir(),file_name);

        Querarr = readQueryFile(file);


//        myQueries.add(new Query("Ttile1","cse101","1","1",new ArrayList<Message>()));
//        myQueries.add(new Query("MAD","cse201","1","1",new ArrayList<Message>()));
//        myQueries.add(new Query("MC","cse301","1","1",new ArrayList<Message>()));

        String currentCourseId  =  getIntent().getStringExtra(AllCoursesActivity.COURSE_ID_EXTRA );

        adapter = new QueryAdapter(Querarr , currentCourseId );

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager( mContext ));
    }


    ArrayList<Query> readQueryFile(File file)
    {
        ObjectInputStream ois = null;

        try
        {
            ois = new ObjectInputStream(new FileInputStream(file));// input the read file.
            Log.d("FILE_FUNC","Object Input stream opened...");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Log.d("FILE_FUNC","Object Input did not stream opened...");
        }

        ArrayList<Query> Querarr = new ArrayList<Query>();

        try
        {
            Querarr = (ArrayList<Query>) ois.readObject() ;
            Log.d("FILE_FUNC","File read of size "+Querarr.size());
        }
        catch (ClassNotFoundException e)
        {
            Log.d("FILE_FUNC","File read me Class not found");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            Log.d("FILE_FUNC","File read me IO");
            e.printStackTrace();
        }
        catch (NullPointerException n)
        {
            n.printStackTrace();
            Log.d("FILE_FUNC","File read me null pointer");
        }

        try
        {
            ois.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException n)
        {
            n.printStackTrace();
        }

        return Querarr;
    }
}
