package com.example.kushagra.meetupapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

public class Constants extends AppCompatActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState)
    {
        super.onCreate(savedInstanceState, persistentState);

    }

    public static final String  SHARED_PREF_FILE_NAME = "com.example.kushagra.mySharedPref";



    public static final String IP_ADD = "http://192.168.55.147:8080";
    public static final String TAG = "SP_Main";

    public static final String IS_LOGGED_IN_EXTRA = "com.exmaple.kushagra.isLoggedIn";
    public static final String USER_NAME_EXTRA = "com.exmaple.kushagra.userName";
    public static final String EMAIL_ID_EXTRA = "com.exmaple.kushagra.emailId";
    public static final String COURSE_ID_EXTRA = "com.exmaple.kushagra.courseId";
    public static final String COURSE_NAME_EXTRA = "com.exmaple.kushagra.courseName";

    public static final String RECYCLER_VIEW_QUERY_ID_EXTRA = "com.exmaple.kushagra.queryId";

    public static final String IS_TA_SELECTED_EXTRA = "com.exmaple.kushagra.isTasELECTED";
    public static final String IS_DESCREPANCY_EXTRA  = "com.exmaple.kushagra.isdiscrepamcy";


    public static final String PROFILE_IMAGE_FILE_URL = "com_exmaple_kushagra_profileImage.png";
    public static final Integer OLD_MESSAGE_NOTIFICATION_ID = 1;
    public static final Integer NEW_QUERY_NOTIFICATION_ID = 2;

}
