package com.example.kushagra.meetupapp;

import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Date;

public class TaFollowUpQueryActivity extends AppCompatActivity {

    int day, month, year, hour, minute;
    String meetingvenue;
    Boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ta_follow_up_query);
    }

    public void onMeetClicked(View view)
    {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TaFollowUpQueryActivity.this);
        dialogBuilder.setTitle("Set Meeting...");
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.meeting, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();

        final EditText venue = (EditText) dialogView.findViewById(R.id.venue);

        final DatePicker date = (DatePicker) dialogView.findViewById(R.id.date);
        final TimePicker time = (TimePicker) dialogView.findViewById(R.id.time);
        venue.clearFocus();

        date.setMinDate(System.currentTimeMillis()-1000);

        Button button_save = (Button) dialogView.findViewById(R.id.button_save);
        Button button_discard = (Button) dialogView.findViewById(R.id.button_discard);

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(venue.getText().toString().isEmpty())
                {
                    venue.setHint("Venue (Required)");
                }
                else
                {
                    meetingvenue = venue.getText().toString();
                    day = date.getDayOfMonth();
                    month = date.getMonth();
                    year = date.getYear();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        hour = time.getHour();
                    } else {
                        hour = time.getCurrentHour();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        minute = time.getMinute();
                    } else {
                        minute = time.getCurrentMinute();
                    }
                    flag=false;
                    Toast.makeText(getApplicationContext(),day+" "+ month+" "+ year+" "+ hour+" "+ minute+" ", Toast.LENGTH_LONG).show();
                    alertDialog.cancel();
                }
            }
        });
        button_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=false;
                alertDialog.cancel();
            }
        });
        flag=true;
        alertDialog.show();
    }
}