package com.example.kushagra.meetupapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.kushagra.meetupapp.db.DbManipulate;
import com.example.kushagra.meetupapp.db.objects.Course;
import com.example.kushagra.meetupapp.background.PingService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class AllCoursesActivity extends AppCompatActivity
{
    public static final String  SHARED_PREF_FILE_NAME = "com.example.kushagra.mySharedPref";



    public static final String IP_ADD = "http://192.168.66.75:8080";
    public static final String TAG = "SP_Main";

    public static final String IS_LOGGED_IN_EXTRA = "com.exmaple.kushagra.isLoggedIn";
    public static final String USER_NAME_EXTRA = "com.exmaple.kushagra.userName";
    public static final String EMAIL_ID_EXTRA = "com.exmaple.kushagra.emailId";
    public static final String COURSE_ID_EXTRA = "com.exmaple.kushagra.courseId";
    public static final String COURSE_NAME_EXTRA = "com.exmaple.kushagra.courseName";

    public static final String RECYCLER_VIEW_QUERY_ID_EXTRA = "com.exmaple.kushagra.queryId";

    public static final String IS_TA_SELECTED_EXTRA = "com.exmaple.kushagra.isTasELECTED";
    public static final String IS_DESCREPANCY_EXTRA  = "com.exmaple.kushagra.isdiscrepamcy";


    public static final Integer OLD_MESSAGE_NOTIFICATION_ID = 1;
    public static final Integer NEW_QUERY_NOTIFICATION_ID = 2;


    private Context mContext;
    RecyclerView list;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses);

        mContext = getApplicationContext();
        DbManipulate dbManipulate = new DbManipulate(getApplicationContext());

    /*

        String s = "destruction";
        dbManipulate.insertMessageOfQuery(new Message("me" ,"you" ,"go to hell") , s );
        dbManipulate.insertMessageOfQuery(new Message("me2" ,"you2" ,"go to hell2") , s );


        ArrayList<Message> msg = dbManipulate.getAllMessagesOfQueryId(s);
        Log.d(MainActivity.TAG , "Came here" + msg.size() );


        for (Message message : msg )
        {
            Log.d(MainActivity.TAG , message.getSender() + " ====" + message.getTaId());
        }

*/

        //get all the courses
        list = (RecyclerView) findViewById(R.id.list);

        ArrayList<Course> myCourses;

        myCourses = dbManipulate.getMyCourses();

        AllCourseAdapter adapter = new AllCourseAdapter(myCourses , getApplicationContext());

        File file = new File(getApplicationContext().getFilesDir(),"CSEWEE");
        if(!file.exists())
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        myCourses.add(new Course("CSE101","CSEWEE",null));
        list.setAdapter(adapter);


        Intent i = new Intent( this , PingService.class);
        startService(i);
        stopService(i);

    }

}
