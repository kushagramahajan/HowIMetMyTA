package com.example.kushagra.meetupapp.background;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.kushagra.meetupapp.AllCoursesActivity;
import com.example.kushagra.meetupapp.Message;
import com.example.kushagra.meetupapp.R;
import com.example.kushagra.meetupapp.StudentFollowUpQueryActivity;
import com.example.kushagra.meetupapp.db.DbManipulate;
import com.example.kushagra.meetupapp.db.objects.RecentMessages;
import com.example.kushagra.meetupapp.navDrawer.CommonCoursesListActivity;
import com.example.kushagra.meetupapp.network.api.ServerApi;
import com.example.kushagra.meetupapp.network.model.StatusClass;
import com.example.kushagra.meetupapp.network.model.TaNewMessage;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.media.RingtoneManager.getDefaultUri;

/**
 * Created by Himanshu Sagar on 28-11-2016.
 */

public class PingService extends Service
{
    String[] newQueries,oldQueries;
    String[] oldCourseIds;

    Intent myIntent;
    int myFlags;
    int myStartId;
    boolean mainFlag  = false;



    private static boolean flag_new = true;
    private static boolean flag_old = true;

    DbManipulate dbManipulate;

    final String TAG = "SPTag";


    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {



        dbManipulate = new DbManipulate(getApplicationContext());


        Intent in = new Intent(PingService.this, CommonCoursesListActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);



        Thread t = new Thread()
        {
            @Override
            public void run()
            {
                checkforPendingMessages();

            }
        };
        t.setName("PingServiceThread");
        t.start();


        Log.d(TAG, "Service Chal rhi h");

/*        while(true)
        {
            if (flag_new && flag_old)
            {

                flag_old = false;
                flag_new = false;


            }

        /*    try
            {
               Thread.sleep(100);

            }
            catch (InterruptedException ie)
            {
                ie.printStackTrace();
                Log.d(AllCoursesActivity.TAG, "Service Stopped");
//                return START_STICKY;

            }
*/


        return START_STICKY;

    }



    private void checkforPendingMessages()
    {
        final CountDownLatch latch = new CountDownLatch(3);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AllCoursesActivity.IP_ADD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServerApi service = retrofit.create(ServerApi.class);

        SharedPreferences editor = getApplicationContext()
                .getSharedPreferences( AllCoursesActivity.SHARED_PREF_FILE_NAME, MODE_PRIVATE);

        Call<StatusClass> call = service.getStatus( new StatusClass(editor.getString(AllCoursesActivity.EMAIL_ID_EXTRA,"default@de.com")));

        Log.d(TAG , "inside check pending request");


        call.enqueue(new Callback<StatusClass>() {
            @Override
            public void onResponse(Call<StatusClass> call, Response<StatusClass> response)
            {

                if(response.body()!=null)
                {
                    boolean isAnyOld = response.body().isAnyOld();
                    boolean isAnyNew = response.body().isAnyNew();

                    oldQueries = response.body().getOldQueryId();
                    newQueries = response.body().getNewQueryId();

                    String[] newCourseIds = response.body().getNewCourseIds();
                    String[] oldCourseIds = response.body().getOldCourseIds();



                    if (isAnyNew == true)
                    {                //ta
                        getPendingNewQueries(newCourseIds , latch);
                    }
                    else if(!isAnyNew)
                    {
                        latch.countDown();
                    }

                    if (isAnyOld == true)
                    {
                        getPendingOldQueries(oldCourseIds, latch);
                    }
                    else if(!isAnyOld)
                    {
                        latch.countDown();

                    }



                }
                else
                {
                    Log.d(TAG , "Response Body null for status check during ping");


                }
                latch.countDown();


            }

            @Override
            public void onFailure(Call<StatusClass> call, Throwable t)
            {
                Log.d(TAG , "Failure during status check ping"  + call.toString() );
                latch.countDown();
                latch.countDown();
                latch.countDown();
            }
        });



        try
        {
            latch.await();  //main thread is waiting on CountDownLatch to finish
            Thread.sleep(2000);

            Log.d( TAG  , "All Booleans are up, Again now");



            checkforPendingMessages();

        }
        catch(InterruptedException ie)
        {
            ie.printStackTrace();
        }

    }

    private void getPendingNewQueries(String[] newCourseIds ,final CountDownLatch latch)
    {


        Log.d( TAG, "Getting new pending queries ");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AllCoursesActivity.IP_ADD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServerApi service = retrofit.create(ServerApi.class);

        SharedPreferences sp = getSharedPreferences(AllCoursesActivity.SHARED_PREF_FILE_NAME , Context.MODE_PRIVATE);
        final String studentId = sp.getString(AllCoursesActivity.EMAIL_ID_EXTRA , "Default");


        // sending individual query ids for new
        //handling by ta

        for(int i=0;i<newQueries.length;i++)
        {
            final String temp=newQueries[i];


            TaNewMessage queryid=new TaNewMessage(newQueries[i]);
            final String queryIdToInsert = queryid.getQueryId();

            Call<TaNewMessage> call = service.getPendingNewQueryList(queryid);

            call.enqueue(new Callback<TaNewMessage>() {
                @Override
                public void onResponse(Call<TaNewMessage> call, Response<TaNewMessage> response) {



                    if (response.body() != null) {

                        TaNewMessage messforaquery = response.body();

                        //handle the array of messages returned
                        String courseid = messforaquery.getCourseId();
                        String description=messforaquery.getDescription();
                        String title = messforaquery.getTitle();
                        String queryid = queryIdToInsert;

// public TaNewMessage(String taId, String studentId, String courseId, String title, String description, boolean isResolved, String queryId) {

                        TaNewMessage taNewMessage = new TaNewMessage( messforaquery.getTaId()  ,
                                messforaquery.getStudentId() , messforaquery.getCourseId() ,
                                title ,description , false , queryIdToInsert);

                        dbManipulate.insertTAQueries(taNewMessage);
                        Log.d(TAG , "New Query" + messforaquery.getCourseId() + " " + messforaquery.getTitle()
                                + messforaquery.getTaId() );

                        Log.d(TAG , dbManipulate.getAllTAQueries(courseid).size()
                                + "--" + dbManipulate.getAllTAQueries(courseid).get(0).getTitle() );


                    } else {
                        Log.d(TAG, "Response Body null");

                    }


                    latch.countDown();
                    flag_new = true;


                }

                @Override
                public void onFailure(Call<TaNewMessage> call, Throwable t) {

                    Log.d(TAG, "Failure to get new messages for newquery" + call.toString());

                    latch.countDown();
                }
            });
        }



    }

    private void getPendingOldQueries(final String[] oldCourseIds ,final CountDownLatch latch)
    {
        Log.d(TAG,  "getting old pending queries ");


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



            final int indexOfQuery = i;
            final String QueryId=oldQueries[i];
            RecentMessages rmessage=new RecentMessages(oldQueries[i],null);
            Call<RecentMessages> call = service.getPendingOldQueryList(rmessage);

            call.enqueue(new Callback<RecentMessages>()
            {
                @Override
                public void onResponse(Call<RecentMessages> call, Response<RecentMessages> response) {

                    if (response.body() != null) {

                        RecentMessages messquery = response.body();
                        Message[] messforaquery= messquery.getMessages();

                        for(int j=0;j<messforaquery.length;j++)
                        {

                            Message insertableMsg = new Message(messforaquery[j].getSender() ,
                                    messforaquery[j].getReceiver() , messforaquery[j].getMessage() ,
                                    messforaquery[j].getQueryId());

                            Log.d(TAG , insertableMsg.getSender() + "says" + insertableMsg.getMessage() );
                            dbManipulate.insertMessageOfQuery(insertableMsg , QueryId);

                        }

                        String courseId = oldCourseIds[indexOfQuery];

                        generateNotificationOldMessage(QueryId,messforaquery,indexOfQuery,courseId);


                    } else {
                        Log.d(TAG, "Response Body null");

                    }
                    flag_old = true;
                    latch.countDown();


                }

                @Override
                public void onFailure(Call<RecentMessages> call, Throwable t)
                {
                    Log.d(TAG, "Failure to get old messages for query" + call.toString());

                    latch.countDown();
                }
            });


        }

    }


    private void generateNotificationOldMessage(String queryId,Message[] messages,int index,String courseId)
    {

/*
        Intent resultIntent = new Intent(PingService.this, StudentFollowUpQueryActivity.class);



//**add this line**
        int requestID = (int) System.currentTimeMillis();



//**add this line**
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

//**edit this line to put requestID as requestCode**
        PendingIntent contentIntent = PendingIntent.getActivity(this, requestID,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);



        resultIntent.putExtra(AllCoursesActivity.RECYCLER_VIEW_QUERY_ID_EXTRA, queryId);

        resultIntent.putExtra(AllCoursesActivity.COURSE_ID_EXTRA, courseId);


        resultIntent.putExtra( AllCoursesActivity.IS_DESCREPANCY_EXTRA , true);





        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_popup_reminder)
                        .setContentTitle( messages.length + "Unread Messages")
                        .setContentText(courseId);

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();


// Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("New Messages :");
// Moves events into the expanded layout
        for (int i=0; i < messages.length; i++) {

            inboxStyle.addLine(messages[i].getMessage());
        }
// Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);
// Creates an explicit intent for an Activity in your app


// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
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
        mNotificationManager.notify(1, mBuilder.build());


*/





        String notificationMessage = "typo";
        Integer NOTIFICATION_ID = 1;

        NotificationManager mNotificationManager =
                (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

        int requestID = (int) System.currentTimeMillis();

         Uri alarmSound = getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

         Intent notificationIntent = new Intent(getApplicationContext(), StudentFollowUpQueryActivity.class);

        notificationIntent.putExtra(AllCoursesActivity.RECYCLER_VIEW_QUERY_ID_EXTRA, queryId);

        notificationIntent.putExtra(AllCoursesActivity.COURSE_ID_EXTRA, courseId);


        notificationIntent.putExtra( AllCoursesActivity.IS_DESCREPANCY_EXTRA , true);



        //**add this line**
         notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

         //**edit this line to put requestID as requestCode**
         PendingIntent contentIntent = PendingIntent.getActivity(this, requestID,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

         NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
         .setSmallIcon(R.drawable.logo)
         .setContentTitle("My Notification")
         .setStyle(new NotificationCompat.BigTextStyle()
         .bigText(notificationMessage))
         .setContentText(notificationMessage).setAutoCancel(true);

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();


// Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Unread Messages");
// Moves events into the expanded layout
        for (int i=0; i < messages.length; i++) {

            inboxStyle.addLine(messages[i].getMessage());
        }
// Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);

         mBuilder.setSound(alarmSound);
         mBuilder.setContentIntent(contentIntent);
         mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
