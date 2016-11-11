package com.example.kushagra.meetupapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class StartNewTaQueryActivity extends AppCompatActivity {

    MultiAutoCompleteTextView students;
    int year,month,day,fHour,fMinute,tHour, tMinute;
    TextView totime, fromtime, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_new_ta_query);

        students = (MultiAutoCompleteTextView) findViewById(R.id.students);
        totime = (TextView) findViewById(R.id.totime);
        fromtime = (TextView) findViewById(R.id.fromtime);
        date = (TextView) findViewById(R.id.date);
        //suggestions  = all students of that cousre
        ArrayList<String> suggestion = new ArrayList<>();
        ArrayAdapter<String> course_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,suggestion);

        students.setAdapter(course_adapter);
        students.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer() );
    }

    public void clickToTime(View v)
    {
        final Calendar c = Calendar.getInstance();
        tHour = c.get(Calendar.HOUR_OF_DAY);
        tMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        totime.setText(hourOfDay + ":" + minute);
                    }
                }, tHour, tMinute, false);
        timePickerDialog.show();
    }
    public void clickFromTime(View v)
    {
        final Calendar c = Calendar.getInstance();
        fHour = c.get(Calendar.HOUR_OF_DAY);
        fMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        fromtime.setText(hourOfDay + ":" + minute);
                    }
                }, fHour, fMinute, false);
        timePickerDialog.show();
    }
    public void clickDate(View v)
    {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, year, month, day);
        datePickerDialog.show();
    }
}