package com.example.kushagra.meetupapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.kushagra.meetupapp.db.DbManipulate;
import com.example.kushagra.meetupapp.db.objects.Course;

import java.util.ArrayList;


public class AllCoursesActivity extends AppCompatActivity
{
    public static final String  SHARED_PREF_FILE_NAME = "com.example.kushagra.mySharedPref";

    public static final String IP_ADD = "http://192.168.55.147:8080";

    public static final String IS_LOGGED_IN_EXTRA = "com.exmaple.kushagra.isLoggedIn";
    public static final String USER_NAME_EXTRA = "com.exmaple.kushagra.userName";
    public static final String EMAIL_ID_EXTRA = "com.exmaple.kushagra.emailId";
    public static final String COURSE_ID_EXTRA = "com.exmaple.kushagra.courseId";
    public static final String COURSE_NAME_EXTRA = "com.exmaple.kushagra.courseName";

    private Context mContext;
    RecyclerView list;





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses);

        mContext = getApplicationContext();

        //get all the courses
        list = (RecyclerView) findViewById(R.id.list);

        ArrayList<Course> myCourses;

        DbManipulate dbManipulate = new DbManipulate(getApplicationContext());
                myCourses = dbManipulate.getMyCourses();

                    AllCourseAdapter adapter = new AllCourseAdapter(myCourses);

                    list.setAdapter(adapter);
                    list.setLayoutManager(new LinearLayoutManager( mContext ));







    }

}
