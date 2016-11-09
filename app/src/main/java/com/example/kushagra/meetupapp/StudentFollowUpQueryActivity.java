package com.example.kushagra.meetupapp;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.kushagra.meetupapp.db.DbContract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class StudentFollowUpQueryActivity extends AppCompatActivity {

    EditText Editmessege;
    LinearLayout msg_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_follow_up_query);
        Editmessege=(EditText)findViewById(R.id.message);
        msg_list = (LinearLayout)findViewById(R.id.msg_list);

        // get the particular query object

        FileInputStream fileIn = null;// Read serial file.
        try {
            SharedPreferences sharedPreferences = getSharedPreferences( AllCoursesActivity.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
            String file_name=sharedPreferences.getString(AllCoursesActivity.COURSE_NAME_EXTRA,"default course name");
            fileIn = new FileInputStream(new File(this.getFilesDir(), file_name));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream in = null;// input the read file.
        try {
            in = new ObjectInputStream(fileIn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<Query> Querarr= null;
        try {
            Querarr = (ArrayList<Query>) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int position=Integer.parseInt(getIntent().getStringExtra("position"));

        Query modquer=Querarr.get(position);
        ArrayList<Messege> messArr=modquer.getMesseges();

        // proper display of the messages for a query

//        messArr = new ArrayList<>();
//        messArr.add(new Messege("ta","me","i wanna drink ur blood"));
//        messArr.add(new Messege("me","ta","no i wanna drink urs, plz"));
//        messArr.add(new Messege("ta","me","no, i wanna drink ur blood, or else grade reduction"));
//        messArr.add(new Messege("me","ta","ok. i'll just gonna cut me-self"));

        for(com.example.kushagra.meetupapp.Messege m : messArr)
        {
            View view;
            view = getLayoutInflater().inflate(R.layout.msg_balloon,null );
            TextView t = (TextView)view.findViewById(R.id.msg_text);
            t.setTextAlignment(m.getSender().equals("me")?View.TEXT_ALIGNMENT_TEXT_END:View.TEXT_ALIGNMENT_TEXT_START);
            t.setText(m.getMessage());
            if(m.getSender().equals("me"))
            {
                t.setTextColor(Color.rgb(153,0,0));
            }
            else
            {
                t.setTextColor(Color.rgb(0,0,153));
            }
            msg_list.addView(view);
        }

    }


    public void clickSendMessege(View v) throws IOException, ClassNotFoundException {
        String messege=Editmessege.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences( AllCoursesActivity.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
        String file_name=sharedPreferences.getString(AllCoursesActivity.COURSE_NAME_EXTRA,"default course name");

        FileInputStream fileIn = new FileInputStream(new File(this.getFilesDir(), file_name));// Read serial file.
        FileOutputStream fileOut = new FileOutputStream(new File(this.getFilesDir(), file_name));

        ObjectInputStream in = new ObjectInputStream(fileIn);// input the read file.
        ObjectOutputStream out=new ObjectOutputStream(fileOut);

        ArrayList<Query> Querarr= (ArrayList<Query>) in.readObject();
        int position=Integer.parseInt(getIntent().getStringExtra("position"));

        Query modquer=Querarr.get(position);
        Messege toadd= new Messege(AllCoursesActivity.EMAIL_ID_EXTRA,modquer.getReceiver(),messege);


        //check wheher posotion sender 0 or 1
        modquer.getMesseges().add(toadd);
        Querarr.set(position,modquer);

        //write the Querarr

        out.writeObject(Querarr);
        if(in!=null)
            in.close();
        if(out!=null)
            out.close();
        fileIn.close();
        fileOut.close();

    }



}
