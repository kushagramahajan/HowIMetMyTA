package com.example.kushagra.meetupapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.kushagra.meetupapp.db.DbContract;
import com.example.kushagra.meetupapp.db.DbManipulate;
import com.example.kushagra.meetupapp.db.objects.*;
import com.example.kushagra.meetupapp.extra.SignInActivity;
import com.example.kushagra.meetupapp.network.api.ServerApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AllCoursesActivity.IP_ADD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServerApi service = retrofit.create(ServerApi.class);
        ArrayList<String> allCourses = null;
        Call< ArrayList<String> > call = service.getAllCoursesList();

        Log.d(MainActivity.TAG , "Inside Fetch");

        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response)
            {
                Log.d(MainActivity.TAG , response.body().size()+" " );

                if(response.body()!=null)
                {
                    ArrayList<com.example.kushagra.meetupapp.db.objects.Course> clist = new ArrayList<com.example.kushagra.meetupapp.db.objects.Course>();
                    ArrayList<String> allcoures = response.body();

                    Log.d(MainActivity.TAG , "Inner part" + response.body() );
                    for(int i=0;i<allcoures.size();i++){
                        String cid=allcoures.get(i).split(";")[0];
                        String cname=allcoures.get(i).split(";")[1];
                        com.example.kushagra.meetupapp.db.objects.Course ctemp=new com.example.kushagra.meetupapp.db.objects.Course(cid,cname,null);
                        clist.add(ctemp);
                    }

                    DbManipulate dbMan=new DbManipulate(getApplicationContext());
                    dbMan.insertAllCourses(clist);

                    SharedPreferences sharedPreferences = getSharedPreferences( AllCoursesActivity.SHARED_PREF_FILE_NAME, MODE_PRIVATE);


                    if(sharedPreferences.getBoolean(AllCoursesActivity.IS_LOGGED_IN_EXTRA , false))
                    {

                        Intent intent = new Intent(getApplicationContext(), AllCoursesActivity.class);
                        startActivity(intent);

                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                        startActivity(intent);
                    }

                }
                else
                {
                    Log.d(MainActivity.TAG , "Response Body null");

                }

            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t)
            {
                Log.d(MainActivity.TAG , "Failure to get  queries"  + call.toString() );

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, StartNewStudentQueryActivity.class);

                i.putExtra(AllCoursesActivity.COURSE_NAME_EXTRA,getIntent().getStringExtra(AllCoursesActivity.COURSE_NAME_EXTRA));

                startActivity(i);
            }
        });


        //get all the queries
        list = (RecyclerView) findViewById(R.id.list);

        ArrayList<Query> myQueries = new ArrayList<>();
        String file_name=getIntent().getStringExtra(AllCoursesActivity.COURSE_NAME_EXTRA);

        FileInputStream fileIn = null;// Read serial file.
        try {
            fileIn = new FileInputStream(new File(this.getFilesDir(),file_name));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream in = null;// input the read file.
        try {
            in = new ObjectInputStream(fileIn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ArrayList<Query> Querarr= (ArrayList<Query>) in.readObject();// allocate it to the object file already instanciated.
            myQueries=Querarr;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        myQueries.add(new Query("Ttile1","cse101",new ArrayList<Messege>()));
//        myQueries.add(new Query("MAD","cse201",new ArrayList<Messege>()));
//        myQueries.add(new Query("MC","cse301",new ArrayList<Messege>()));

        QueryAdapter adapter = new QueryAdapter(myQueries);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager( mContext ));
    }

}
