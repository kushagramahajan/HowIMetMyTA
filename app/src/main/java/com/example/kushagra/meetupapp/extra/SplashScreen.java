package com.example.kushagra.meetupapp.extra;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.kushagra.meetupapp.AllCoursesActivity;
import com.example.kushagra.meetupapp.MainActivity;
import com.example.kushagra.meetupapp.Query;
import com.example.kushagra.meetupapp.R;
import com.example.kushagra.meetupapp.background.PingService;
import com.example.kushagra.meetupapp.db.DbManipulate;
import com.example.kushagra.meetupapp.db.objects.Course;
import com.example.kushagra.meetupapp.navDrawer.CommonCoursesListActivity;
import com.example.kushagra.meetupapp.network.api.ServerApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashScreen extends AppCompatActivity
{
    private final int SPLASH_DISPLAY_LENGTH = 1000;


    public static boolean isOnline()
    {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    private void fetchFromServerUpdateDB()
    {
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
                    ArrayList<Course> clist = new ArrayList<Course>();
                    ArrayList<String> allcoures = response.body();

                    Log.d(MainActivity.TAG , "Inner part" + response.body() );

                    for(int i=0;i<allcoures.size();i++)
                    {
                        String cid=allcoures.get(i).split(";")[0];
                        String cname=allcoures.get(i).split(";")[1];
                        Course ctemp=new Course(cid,cname,null);
                        clist.add(ctemp);
                    }

                    DbManipulate dbMan=new DbManipulate(getApplicationContext());
                    dbMan.insertAllCourses(clist);

                    SharedPreferences sharedPreferences = getSharedPreferences( AllCoursesActivity.SHARED_PREF_FILE_NAME, MODE_PRIVATE);


                    if(sharedPreferences.getBoolean(AllCoursesActivity.IS_LOGGED_IN_EXTRA , false))
                    {

                        Intent intent = new Intent(getApplicationContext(), CommonCoursesListActivity.class);
                        startActivity(intent);

                    }
                    else
                    {

                        Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                        startActivity(intent);

                    }

                        /*

                    CAll to db manipulate update All courses


                     */

                }
                else
                {
                    Log.d(MainActivity.TAG , "Response Body null");

                }

            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t)
            {
                Log.d(MainActivity.TAG , "Failure"  + call.toString() );

            }
        });


        // sending post request for any qeusries


    }

    private boolean isNotLoggedIn()
    {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                AllCoursesActivity.SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        return !(sharedPreferences.getBoolean(AllCoursesActivity.IS_LOGGED_IN_EXTRA, false));
    }


    private void removeService()
    {

        Intent i = new Intent(this , PingService.class);
        try
        {
            stopService(i);
        }
        catch (Exception e)
        {
            Log.d(MainActivity.TAG , "Refreshing Service");

        }
    }

    private void initiateService()
    {

        Log.d(MainActivity.TAG , "Inside init");
        Intent i = new Intent( SplashScreen.this , PingService.class);
        // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(i);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();



        if(isNotLoggedIn() && isOnline())
        {
            fetchFromServerUpdateDB();
            Log.d(AllCoursesActivity.TAG , "Splash NotLoggedIn isOnline");
        }
        else if( !isNotLoggedIn() && isOnline())
        {
            Log.d(AllCoursesActivity.TAG , "Splash Going ahead");


            initiateService();

            Log.d(MainActivity.TAG , "Starting Intent");


            //  removeService();



        }
        else if(isNotLoggedIn() && (!isOnline()) )
        {
            Toast.makeText( this , "NO Internet",
                    Toast.LENGTH_LONG).show();

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    finish();

                }
            }, SPLASH_DISPLAY_LENGTH);


        }
        else if(!isNotLoggedIn())
        {

            Intent intent = new Intent(this , CommonCoursesListActivity.class);
            startActivity(intent);


        }


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


}
