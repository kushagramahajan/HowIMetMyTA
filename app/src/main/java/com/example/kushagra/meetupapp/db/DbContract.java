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

    public static class CourseQueryMapping implements BaseColumns
    {
        public static final String TABLE_NAME = "CourseQuery";

        public static final String COLUMN_NAME_COURSE_ID = "cqcourseId";
        public static final String COLUMN_NAME_QUERY_ID = "cqqueryId";

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

    public static class TableMessages implements BaseColumns
    {

        public static final String TABLE_NAME = "TableMessages";
        public static final String COLUMN_NAME_MESSAGE_RECEIVER = "messageReceiver";
        public static final String COLUMN_NAME_MESSAGE_SENDER = "messageSender";
        public static final String COLUMN_NAME_MESSAGE_STRING = "messageString";
        public static final String COLUMN_NAME_MESSAGE_QUERY_ID = "queryId";

    }

    public static class TableTASideQueries implements BaseColumns
    {
        public static final String TABLE_NAME = "TableQueries";
        public static final String COLUMN_NAME_COURSE_ID = "tacourseIdx";

        public static final String COLUMN_NAME_DESCRIPTION = "tacourseDesp";
        public static final String COLUMN_NAME_TITLE = "taquerytitle";
        public static final String COLUMN_NAME_TA_ID = "tataId";
        public static final String COLUMN_NAME_STUDENT_ID = "tastudentId";

        public static final String COLUMN_NAME_QUERY_ID = "taqueryID";

    }

    public static class TASideMyCourses implements BaseColumns
    {
        public static final String TABLE_NAME = "TAMyCourses";
        public static final String COLUMN_NAME_COURSE_NAME = "TAMyName";
        public static final String COLUMN_NAME_COURSE_ID = "TAMyId";


    }


}
