package com.example.kushagra.meetupapp;


import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.kushagra.meetupapp.db.DbContract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class StudentFollowUpQueryActivity extends AppCompatActivity {

    EditText Editmessege;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Editmessege=(EditText)findViewById(R.id.messege);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_follow_up_query);

        // get the particular query object

        FileInputStream fileIn = null;// Read serial file.
        try {
            fileIn = new FileInputStream(new File(this.getFilesDir(), AllCoursesActivity.COURSE_NAME_EXTRA));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream in = null;// input the read file.
        try {
            in = new ObjectInputStream(fileIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Query> Querarr= null;
        try {
            Querarr = (ArrayList<Query>) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int position=Integer.parseInt(getIntent().getStringExtra("position"));

        Query modquer=Querarr.get(position);
        ArrayList<Messege> messArr=modquer.getMesseges();

        // proper display of the messages


    }


    public void clickSendMessege(View v) throws IOException, ClassNotFoundException {
        String messege=Editmessege.getText().toString();

        FileInputStream fileIn = new FileInputStream(new File(this.getFilesDir(), AllCoursesActivity.COURSE_NAME_EXTRA));// Read serial file.
        ObjectInputStream in = new ObjectInputStream(fileIn);// input the read file.
        ArrayList<Query> Querarr= (ArrayList<Query>) in.readObject();
        int position=Integer.parseInt(getIntent().getStringExtra("position"));

        Query modquer=Querarr.get(position);
        Messege toadd= new Messege(AllCoursesActivity.EMAIL_ID_EXTRA,modquer.getReceiver(),messege);


        //check wheher posotion from 0 or 1
        modquer.getMesseges().add(toadd);
        Querarr.set(position,modquer);

    }



}
