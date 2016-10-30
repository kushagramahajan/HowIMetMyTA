package com.example.kushagra.meetupapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Himanshu Sagar on 20-10-2016.
 */


public class DbHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MeetUp.db";

    private static final String TEXT_TYPE = " TEXT NOT NULL";
    private static final String INT_TYPE = " INTEGER NOT NULL";

    private static final String COMMA_SEP = ",";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String SQL_CREATE_ENTRIES =
            " CREATE TABLE " + DbContract.ScheduleEntry.TABLE_NAME + " (" +
                    DbContract.ScheduleEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    DbContract.ScheduleEntry.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
                    DbContract.ScheduleEntry.COLUMN_NAME_DAY + TEXT_TYPE + COMMA_SEP +
                    DbContract.ScheduleEntry.COLUMN_NAME_TIME_BEGIN + TEXT_TYPE + COMMA_SEP +
                    DbContract.ScheduleEntry.COLUMN_NAME_TIME_END + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DbContract.ScheduleEntry.TABLE_NAME;


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES_ALL);
        db.execSQL(SQL_CREATE_ENTRIES_MY);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRIES_ALL);
        db.execSQL(SQL_DELETE_ENTRIES_MY);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



    public static final String SQL_CREATE_ENTRIES_ALL =
            "CREATE TABLE " + DbContract.AllCourses.TABLE_NAME + " (" +
                    DbContract.AllCourses.COLUMN_NAME_COURSE_NAME + TEXT_TYPE + COMMA_SEP +
                    DbContract.AllCourses.COLUMN_NAME_COURSE_ID + TEXT_TYPE +
                    " )";

    public static final String SQL_CREATE_ENTRIES_MY =
            "CREATE TABLE " + DbContract.MyCourses.TABLE_NAME + " (" +
                    DbContract.MyCourses.COLUMN_NAME_COURSE_ID + TEXT_TYPE + COMMA_SEP +
                    DbContract.MyCourses.COLUMN_NAME_COURSE_NAME+TEXT_TYPE+
                    " )";



    public static final String SQL_DELETE_ENTRIES_ALL =
            "DROP TABLE IF EXISTS " + DbContract.AllCourses.TABLE_NAME;

    public static final String SQL_DELETE_ENTRIES_MY =
            "DROP TABLE IF EXISTS " + DbContract.MyCourses.TABLE_NAME;


}
