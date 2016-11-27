package com.example.kushagra.meetupapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kushagra.meetupapp.db.DbManipulate;

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

        String file_name = getIntent().getStringExtra(AllCoursesActivity.COURSE_ID_EXTRA);
//        String file_name = getIntent().getStringExtra("MAMA");


        Log.d(MainActivity.TAG , file_name + " ...");

        FileInputStream fileIn = null;// Read serial file.
        try
        {
            fileIn = new FileInputStream(new File(this.getFilesDir(), file_name));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        ObjectInputStream in = null;// input the read file.
        try
        {
            in = new ObjectInputStream(fileIn);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ArrayList<Query> Querarr= null;
        try
        {
            Querarr = (ArrayList<Query>) in.readObject();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException io)
        {
            io.printStackTrace();
        }

        Log.d(MainActivity.TAG , Querarr.size()+ " " +
                "" );


// this is original code to add messages to the array list

//        int position=getIntent().getIntExtra("position",0);
//
//        Query modquer=Querarr.get(position);
//        ArrayList<Message> messArr=modquer.getMessages();


        //used this code to add following shit messages for testing. pleasee remove


        ArrayList<Message> messArr;

        messArr = new ArrayList<>();
        messArr.add(new Message("ta","me","i wanna drink ur blood"));
        messArr.add(new Message("me","ta","no i wanna drink urs, plz"));
        messArr.add(new Message("ta","me","no, i wanna drink ur blood, or else grade reduction"));
        messArr.add(new Message("me","ta","ok. i'll just gonna cut me-self"));


        //code to add message UI
        for(com.example.kushagra.meetupapp.Message m : messArr)
        {
            View view;
            if(m.getSender().equals("me"))
            {
                view = getLayoutInflater().inflate(R.layout.msg_balloon_me,null);
            }
            else
            {
                view = getLayoutInflater().inflate(R.layout.msg_balloon_them,null);
            }
            TextView t = (TextView)view.findViewById(R.id.msg_text);
            t.setText(m.getMessage());
            msg_list.addView(view);
        }

    }


    public void clickSendMessage(View v) throws IOException, ClassNotFoundException
    {
        String message = Editmessege.getText().toString();


        String file_name = getIntent().getStringExtra(AllCoursesActivity.COURSE_ID_EXTRA);
        Log.d(MainActivity.TAG , "inside click Msg  courseID"+ file_name );
        FileInputStream fileIn = new FileInputStream(new File(this.getFilesDir(), file_name));// Read serial file.
        FileOutputStream fileOut = new FileOutputStream(new File(this.getFilesDir(), file_name));

        ObjectInputStream in = new ObjectInputStream(fileIn);// input the read file.
        ObjectOutputStream out=new ObjectOutputStream(fileOut);

        ArrayList<Query> Querarr= (ArrayList<Query>) in.readObject();
        int position = Integer.parseInt(getIntent().getStringExtra("position"));

        Query modquer = Querarr.get(position);


        SharedPreferences sp = getApplicationContext()
                .getSharedPreferences(AllCoursesActivity.SHARED_PREF_FILE_NAME , Context.MODE_PRIVATE);

        String my_emailId = sp.getString(AllCoursesActivity.EMAIL_ID_EXTRA , "default@email.com");

        Log.d(AllCoursesActivity.TAG , "emailId" + my_emailId );
        Message toadd = new Message(
                my_emailId
                ,modquer.getTaId(),message);


        DbManipulate dbman=new DbManipulate(getApplicationContext());
        dbman.insertMessageOfQuery(toadd,modquer.getQueryId());


        //check wheher posotion sender 0 or 1

//        modquer.getMessages().add(toadd);
//        Querarr.set(position,modquer);

        //write the Querarr

//        out.writeObject(Querarr);

        if( in != null)
            in.close();
        if( out != null )
            out.close();

        fileIn.close();
        fileOut.close() ;

    }



}
