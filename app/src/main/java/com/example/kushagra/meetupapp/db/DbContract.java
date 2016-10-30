package com.example.kushagra.meetupapp.db;

import android.provider.BaseColumns;

/**
 * Created by Himanshu Sagar on 20-10-2016.
 */

public final class DbContract
{



    public  enum tableDays { MONDAY ,TUESDAY , WEDNESDAY , THURSDAY , FRIDAY , SATURDAY }

    private DbContract() {}
    public static class ScheduleEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "scheduleEntry";

        public static final String COLUMN_NAME_ID = "scheduleID";
        public static final String COLUMN_NAME_TYPE = "schduleType";
        public static final String COLUMN_NAME_DAY = "scheduleDays";
        public static final String COLUMN_NAME_TIME_BEGIN = "timeBegin";
        public static final String COLUMN_NAME_TIME_END = "timeEnd";


    }

    public static class AllCourses implements BaseColumns
    {
        public static final String TABLE_NAME = "allCourses";

        public static final String COLUMN_NAME_COURSE_NAME = "courseName";
        public static final String COLUMN_NAME_COURSE_ID = "courseId";

    }

    public static class MyCourses implements BaseColumns
    {
        public static final String TABLE_NAME = "MyCourses";

        public static final String COLUMN_NAME_COURSE_NAME = "MyName";
        public static final String COLUMN_NAME_COURSE_ID = "MyId";

    }



}
