package com.example.kushagra.meetupapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kushagra.meetupapp.db.DbManipulate;
import com.example.kushagra.meetupapp.db.objects.ScheduleSlot;

import java.util.ArrayList;

public class ScheduleGenerateActivity extends AppCompatActivity
{

    DbManipulate dbManipulate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        dbManipulate = new DbManipulate(getApplicationContext());

        setContentView(R.layout.activity_form);

    }

    /*
    Day :
    ArrayList< Pair <Srting>>
     */

    private void retrieveTotalSchedule()
    {
        /*
        get from ui;
        pranks
         */
        ArrayList<ScheduleSlot> slots = new ArrayList<>();
        dbManipulate.saveScheduleToDB(slots);



    }


}
