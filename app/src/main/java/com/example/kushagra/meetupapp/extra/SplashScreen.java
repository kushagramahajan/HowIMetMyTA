package com.example.kushagra.meetupapp.extra;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.kushagra.meetupapp.AllCoursesActivity;
import com.example.kushagra.meetupapp.MainActivity;
import com.example.kushagra.meetupapp.Message;
import com.example.kushagra.meetupapp.Query;
import com.example.kushagra.meetupapp.R;
import com.example.kushagra.meetupapp.StudentFollowUpQueryActivity;
import com.example.kushagra.meetupapp.db.DbManipulate;
import com.example.kushagra.meetupapp.db.objects.Course;
import com.example.kushagra.meetupapp.db.objects.RecentMessages;
import com.example.kushagra.meetupapp.navDrawer.CommonCoursesListActivity;
import com.example.kushagra.meetupapp.network.api.ServerApi;
import com.example.kushagra.meetupapp.network.model.StatusClass;
import com.example.kushagra.meetupapp.network.model.TaNewMessage;

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
    String[] newQueries,oldQueries;
    String[] oldCourseIds;

    DbManipulate dbman;

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

    private void checkforPendingMessages()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AllCoursesActivity.IP_ADD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServerApi service = retrofit.create(ServerApi.class);
        SharedPreferences editor = getApplicationContext()
                .getSharedPreferences( AllCoursesActivity.SHARED_PREF_FILE_NAME, MODE_PRIVATE);

        Call<StatusClass> call = service.getStatus( new StatusClass(editor.getString(AllCoursesActivity.EMAIL_ID_EXTRA,"default@de.com")));

        Log.d(MainActivity.TAG , "inside check pending request");


        call.enqueue(new Callback<StatusClass>() {
            @Override
            public void onResponse(Call<StatusClass> call, Response<StatusClass> response)
            {

                if(response.body()!=null)
                {
                    boolean isAnyOld = response.body().isAnyOld();
                    boolean isAnyNew = response.body().isAnyNew();
                    oldQueries=response.body().getOldQueryId();
                    newQueries=response.body().getNewQueryId();

                    if(isAnyOld==true && isAnyNew==false)
                    {              //student
                        getPendingOldQueries();

                    }
                    else if(isAnyNew==true)
                    {                //ta
                        getPendingNewQueries();
                    }


                }
                else
                {
                    Log.d(MainActivity.TAG , "Response Body null for status check during ping");

                }

            }

            @Override
            public void onFailure(Call<StatusClass> call, Throwable t)
            {
                Log.d(MainActivity.TAG , "Failure during status check ping"  + call.toString() );

            }
        });




    }

    private void getPendingNewQueries()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AllCoursesActivity.IP_ADD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServerApi service = retrofit.create(ServerApi.class);

        // sending individual query ids for new
        //handling by ta

        for(int i=0;i<newQueries.length;i++) {
            final String temp=newQueries[i];


            TaNewMessage queryid=new TaNewMessage(newQueries[i]);
            final String queryIdToInsert=queryid.getQueryId();

            Call<TaNewMessage> call = service.getPendingNewQueryList(queryid);
            call.enqueue(new Callback<TaNewMessage>() {
                @Override
                public void onResponse(Call<TaNewMessage> call, Response<TaNewMessage> response) {
                    Log.d(MainActivity.TAG, "inside on response for getting new pending queries ");


                    if (response.body() != null) {

                        TaNewMessage messforaquery = response.body();

                        //handle the array of messages returned
                        String courseid = messforaquery.getCourseId();
                        String description=messforaquery.getDescription();
                        String title = messforaquery.getTitle();
                        String queryid=queryIdToInsert;



                        //////////////////

//                        FileOutputStream fileOut=null;
//                        FileInputStream fileIn=null;
//
//                        try
//                        {
//                            File file = new File(getApplicationContext().getFilesDir(),file_name);
//                            System.out.println("FILENAME SS : "+file.getAbsolutePath());
//                            fileOut=new FileOutputStream(file);
//                        }
//                        catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//
//                        try
//                        {
//                            fileIn = new FileInputStream(new File(getApplicationContext().getFilesDir(),file_name));// Read serial file.
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        boolean flag = false;
//                        ObjectInputStream in = null;
//
//                        try
//                        {in = new ObjectInputStream(fileIn);// input the read file.
//                        }
//                        catch(Exception e)
//                        {
//                            flag=true;
//                        }
//
//                        ArrayList<Query> Querarr = null;
//                        if(!flag)
//                        {
//                            try {
//                                Querarr= (ArrayList<Query>) in.readObject();// allocate it receiver the object file already instanciated.
//                            } catch (ClassNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        else
//                        {
//                            Querarr = new ArrayList<>();
//                        }
//
//                        Query newquery=new Query(temp,messforaquery.getTitle(),messforaquery.getDescription(),messforaquery.getTaId(),new ArrayList<Message>());
//                        Querarr.add(newquery);
//
//                        ObjectOutputStream out=null;
//
//                        try {
//                            out = new ObjectOutputStream(fileOut);
//                            out.writeObject(Querarr);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                    } else {
                        Log.d(MainActivity.TAG, "Response Body null");

                    }

                }

                @Override
                public void onFailure(Call<TaNewMessage> call, Throwable t) {

                    Log.d(MainActivity.TAG, "Failure to get new messages for newquery" + call.toString());

                }
            });
        }



    }

    private void getPendingOldQueries()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AllCoursesActivity.IP_ADD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServerApi service = retrofit.create(ServerApi.class);


//        String file_name=getIntent().getStringExtra(AllCoursesActivity.COURSE_ID_EXTRA );
//        File file = new File(getApplicationContext().getFilesDir(),file_name);
//        ArrayList<Query> Querarr = readQueryFile(file);



        //sending the individual query ids for old

        for(int i=0;i<oldQueries.length;i++)
        {


            final int indexOfQuery=i;
            final String QueryId=oldQueries[i];
            RecentMessages rmessage=new RecentMessages(oldQueries[i],null);
            Call<RecentMessages> call = service.getPendingOldQueryList(rmessage);
            call.enqueue(new Callback<RecentMessages>() {
                @Override
                public void onResponse(Call<RecentMessages> call, Response<RecentMessages> response) {
                    Log.d(MainActivity.TAG, "inside on response for getting old pending queries ");

                    if (response.body() != null) {

                        RecentMessages messquery = response.body();
                        Message[] messforaquery= messquery.getMessages();

                        for(int j=0;j<messforaquery.length;j++)
                            dbman.insertMessageOfQuery(messforaquery[j],QueryId);


//                        FileInputStream fileIn = null;// Read serial file.
//                        FileOutputStream fileOut=null;
//                        try {
//                            SharedPreferences sharedPreferences = getSharedPreferences( AllCoursesActivity.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
//                            String file_name=sharedPreferences.getString(AllCoursesActivity.COURSE_NAME_EXTRA,"default course name");
//
//                            fileIn = new FileInputStream(new File(getApplicationContext().getFilesDir(), file_name));
//                            fileOut= new FileOutputStream(new File(getApplicationContext().getFilesDir(), file_name));
//
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                        ObjectInputStream in = null;// input the read file.
//                        ObjectOutputStream out=null;
//                        try {
//                            in = new ObjectInputStream(fileIn);
//                            out= new ObjectOutputStream(fileOut);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        ArrayList<Query> Querarr= null;
//
//                        try {
//                            Querarr = (ArrayList<Query>) in.readObject();
//
//                        //find sender
//                        String from=messforaquery[0].getSender();
//                        int position=0;
//                        int l;
//                        for(l=0;l<Querarr.size();l++){
//                            if(Querarr.get(l).getTaId().equals(from)){
//                                position=l;
//                                break;
//                            }
//                        }
//                        if(l==Querarr.size()){
//                            Log.d("MainActivity","No query found to insert the message");
//                        }
//                        else {
////                            Query modquer = Querarr.get(position);
////                            ArrayList<Message> messArr = modquer.getMessages();
////
////                            for (int k = 0; k< messforaquery.length; k++) {
////                                messArr.add(messforaquery[k]);
////                            }
////
////                            modquer.setMessages(messArr);
////                            Querarr.set(position, modquer);
//
//                            //handle the array of messages returned
//                            ///
//                            ///
//                            ///
//
//
//
//
//                            out.writeObject(Querarr);
//                            try
//                            {
//                                if(out!=null)
//                                    out.close();
//                                if(in!=null)
//                                    in.close();
//                                fileIn.close();
//                                fileOut.close();
//                            }
//                            catch (Exception e)
//                            {
//                                e.printStackTrace();
//                            }
//
//                        }
//                        } catch (ClassNotFoundException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                        String courseId=dbman.getCourseId(QueryId);
                        generateNotificationOldMessage(QueryId,messforaquery,indexOfQuery,courseId);


                    } else {
                        Log.d(MainActivity.TAG, "Response Body null");

                    }

                }

                @Override
                public void onFailure(Call<RecentMessages> call, Throwable t) {
                    Log.d(MainActivity.TAG, "Failure to get old messages for query" + call.toString());

                }
            });


        }

    }


    private void generateNotificationOldMessage(String queryId,Message[] messages,int index,String courseId){

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_add_black_24dp)
                        .setContentTitle(messages.length+" Message from "+messages[0].getSender()+"!" )
                        .setContentText(messages[messages.length-1].getMessage());


        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, StudentFollowUpQueryActivity.class);
        resultIntent.putExtra(AllCoursesActivity.RECYCLER_VIEW_POSITION_EXTRA, index);
        resultIntent.putExtra(AllCoursesActivity.COURSE_ID_EXTRA, courseId);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(StudentFollowUpQueryActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(index, mBuilder.build());

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
                    for(int i=0;i<allcoures.size();i++){
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        dbman=new DbManipulate(getApplicationContext());

        getSupportActionBar().hide();

        if(isNotLoggedIn() && isOnline())
        {
            fetchFromServerUpdateDB();
            Log.d(AllCoursesActivity.TAG , "Splash NotLoggedIn isOnline");
        }
        else if( !isNotLoggedIn() && isOnline())
        {
            Log.d(AllCoursesActivity.TAG , "Splash Going ahead");

            checkforPendingMessages();

            fetchFromServerUpdateDB();




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
