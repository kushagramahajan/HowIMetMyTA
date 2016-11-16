package com.example.kushagra.meetupapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.kushagra.meetupapp.MainActivity;
import com.example.kushagra.meetupapp.Message;
import com.example.kushagra.meetupapp.db.objects.Course;
import com.example.kushagra.meetupapp.db.objects.ScheduleSlot;

import java.util.ArrayList;

/**
 * Created by Himanshu Sagar on 20-10-2016.
 */


public class DbManipulate
{

    private DbHelper mDbHelper;

    public DbManipulate(Context context)
    {
        mDbHelper = new DbHelper(context);

    }


    public void insertAllCourses(ArrayList<Course>  clist){

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(DbHelper.SQL_DELETE_ENTRIES_ALL);
        db.execSQL(DbHelper.SQL_CREATE_ENTRIES_ALL);

        for(int i=0;i<clist.size();i++) {
            ContentValues values = new ContentValues();

            values.put(DbContract.AllCourses.COLUMN_NAME_COURSE_ID, clist.get(i).getCourseId());
            values.put(DbContract.AllCourses.COLUMN_NAME_COURSE_NAME, clist.get(i).getCourseName());

            db.insert(DbContract.AllCourses.TABLE_NAME, null, values);
        }

    }

    public void insertMyCourses(ArrayList<Course> clist)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(DbHelper.SQL_DELETE_ENTRIES_MY);
        db.execSQL(DbHelper.SQL_CREATE_ENTRIES_MY);

        for(int i=0;i<clist.size();i++) {
            ContentValues values = new ContentValues();

            values.put(DbContract.MyCourses.COLUMN_NAME_COURSE_ID, clist.get(i).getCourseId());
            values.put(DbContract.MyCourses.COLUMN_NAME_COURSE_NAME, clist.get(i).getCourseName());

            db.insert(DbContract.MyCourses.TABLE_NAME, null, values);
        }




    }

    public ArrayList<Course> getMyCourses()
    {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                DbContract.MyCourses.COLUMN_NAME_COURSE_ID,
                DbContract.MyCourses.COLUMN_NAME_COURSE_NAME,
        };


        Cursor cursor = db.query(
                DbContract.MyCourses.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                // The sort order
        );

        cursor.moveToFirst();

        ArrayList<Course> clist = new ArrayList<>();

        while(!cursor.isAfterLast())
        {
            clist.add( new Course(
                    cursor.getString( cursor.getColumnIndex(DbContract.MyCourses.COLUMN_NAME_COURSE_ID) ),
                    cursor.getString( cursor.getColumnIndex(DbContract.MyCourses.COLUMN_NAME_COURSE_NAME) ),null
            ));

            cursor.moveToNext();

        }


        cursor.close();

        return clist;



    }

    public ArrayList<Course> getAllCourses(){

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                DbContract.AllCourses.COLUMN_NAME_COURSE_ID,
                DbContract.AllCourses.COLUMN_NAME_COURSE_NAME,
        };


        Cursor cursor = db.query(
                DbContract.AllCourses.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                // The sort order
        );

        cursor.moveToFirst();

        ArrayList<Course> clist = new ArrayList<>();

        while(!cursor.isAfterLast())
        {
            clist.add( new Course(
                    cursor.getString( cursor.getColumnIndex(DbContract.AllCourses.COLUMN_NAME_COURSE_ID) ),
                    cursor.getString( cursor.getColumnIndex(DbContract.AllCourses.COLUMN_NAME_COURSE_NAME) ),null
                    ));

            cursor.moveToNext();

        }


        cursor.close();

        return clist;



    }



    public void insertScheduleSlot( ScheduleSlot slot)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ScheduleEntry.COLUMN_NAME_TYPE , slot.getType());
        values.put(DbContract.ScheduleEntry.COLUMN_NAME_DAY , slot.getDay());
        values.put(DbContract.ScheduleEntry.COLUMN_NAME_TIME_BEGIN, slot.getTimeBegin());
        values.put(DbContract.ScheduleEntry.COLUMN_NAME_TIME_END, slot.getTimeEnd());


        db.insert(DbContract.ScheduleEntry.TABLE_NAME, null, values);
    }

    public void deleteSchedule( ScheduleSlot slot)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        boolean isDel = db.delete(DbContract.ScheduleEntry.TABLE_NAME, DbContract.ScheduleEntry.COLUMN_NAME_ID + "=" + slot.getType() , null) > 0;
        Log.d(MainActivity.TAG , "Deleted" + slot.getType() + isDel);


    }


    public ArrayList<ScheduleSlot> readSingleSchedule(String particularSchedule)
    {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                DbContract.ScheduleEntry.COLUMN_NAME_ID,
                DbContract.ScheduleEntry.COLUMN_NAME_TYPE,
                DbContract.ScheduleEntry.COLUMN_NAME_DAY,
                DbContract.ScheduleEntry.COLUMN_NAME_TIME_BEGIN,
                DbContract.ScheduleEntry.COLUMN_NAME_TIME_END
        };

// Filter results WHERE "title" = 'My Title'

        String selection = DbContract.ScheduleEntry.COLUMN_NAME_TYPE + " = ?";
        String[] selectionArgs = { particularSchedule };


// How you want the results sorted in the resulting Cursor

        Cursor cursor = db.query(
                DbContract.ScheduleEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                // The sort order
        );

        cursor.moveToFirst();

        ArrayList<ScheduleSlot> slots = new ArrayList<>();

        while(!cursor.isAfterLast())
        {
            slots.add( new ScheduleSlot(
                    cursor.getString( cursor.getColumnIndex(DbContract.ScheduleEntry.COLUMN_NAME_TYPE) ),
                    cursor.getString( cursor.getColumnIndex(DbContract.ScheduleEntry.COLUMN_NAME_DAY) ),
                    cursor.getString( cursor.getColumnIndex(DbContract.ScheduleEntry.COLUMN_NAME_TIME_BEGIN) ),
                    cursor.getString( cursor.getColumnIndex(DbContract.ScheduleEntry.COLUMN_NAME_TIME_END) )
             ));

            cursor.moveToNext();

        }


        cursor.close();

        return slots;
    }

    public void saveScheduleToDB(ArrayList<ScheduleSlot> slots)
    {
        for (ScheduleSlot slot : slots)
        {
            insertScheduleSlot(slot);
        }

    }

    public void updateTimeOfDayInSchedule(ScheduleSlot slot)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ScheduleEntry.COLUMN_NAME_TYPE , slot.getType());
        values.put(DbContract.ScheduleEntry.COLUMN_NAME_DAY , slot.getDay());
        values.put(DbContract.ScheduleEntry.COLUMN_NAME_TIME_BEGIN, slot.getTimeBegin());
        values.put(DbContract.ScheduleEntry.COLUMN_NAME_TIME_END, slot.getTimeEnd());




        db.update(DbContract.ScheduleEntry.TABLE_NAME , values,
                DbContract.ScheduleEntry.COLUMN_NAME_TYPE +" = "+ slot.getType() +
                " AND " +
                DbContract.ScheduleEntry.COLUMN_NAME_DAY + " = " + slot.getDay()
                , null);


        db.close();
    }


    ArrayList<Message> getAllMessagesOfQueryId(String queryId)
    {
        ArrayList<Message> resultMsg = new ArrayList<>();


        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] whereValues = {
                queryId
        };

        String[] whereCols = {
                DbContract.TableMessages.COLUMN_NAME_MESSAGE_QUERY_ID
        };


        Cursor cursor = db.query(
                DbContract.TableMessages.TABLE_NAME,                     // The table to query
                null,                               // The columns to return
                DbContract.TableMessages.COLUMN_NAME_MESSAGE_QUERY_ID,                                // The columns for the WHERE clause
                whereValues,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                // The sort order
        );

        cursor.moveToFirst();


        while(!cursor.isAfterLast())
        {
            resultMsg.add(
                    new Message(
                    cursor.getString( cursor.getColumnIndex(DbContract.TableMessages.COLUMN_NAME_MESSAGE_SENDER) ),
                            cursor.getString( cursor.getColumnIndex(DbContract.TableMessages.COLUMN_NAME_MESSAGE_RECIEVER) ),
                    cursor.getString( cursor.getColumnIndex(DbContract.TableMessages.COLUMN_NAME_MESSAGE_STRING ) )
            ));

            cursor.moveToNext();

        }


        cursor.close();






        return resultMsg;

    }

    //Handle MEssages Corresponding to a specific Query
    void insertMessageOfQuery(Message msg, String queryID)
    {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.TableMessages.COLUMN_NAME_MESSAGE_QUERY_ID , queryID );
        values.put(DbContract.TableMessages.COLUMN_NAME_MESSAGE_SENDER , msg.getSender() );
        values.put(DbContract.TableMessages.COLUMN_NAME_MESSAGE_RECIEVER , msg.getReceiver() );
        values.put(DbContract.TableMessages.COLUMN_NAME_MESSAGE_STRING , msg.getMessage() );

        db.insert(DbContract.TableMessages.TABLE_NAME, null, values);



    }


}

