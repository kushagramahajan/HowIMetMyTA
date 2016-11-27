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
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_follow_up_query);
        Editmessege=(EditText)findViewById(R.id.message);
        msg_list = (LinearLayout)findViewById(R.id.msg_list);

        DbManipulate dbman=new DbManipulate(getApplicationContext());


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


        int position=getIntent().getIntExtra("position",0);
        Query modquer=Querarr.get(position);
//        ArrayList<Message> messArr=modquer.getMessages();


        //used this code to add following shit messages for testing. pleasee remove





        ArrayList<Message> messArr;
        messArr=dbman.getAllMessagesOfQueryId(modquer.getQueryId());



//        messArr = new ArrayList<>();
//        messArr.add(new Message("ta","me","i wanna drink ur blood"));
//        messArr.add(new Message("me","ta","no i wanna drink urs, plz"));
//        messArr.add(new Message("ta","me","no, i wanna drink ur blood, or else grade reduction"));
//        messArr.add(new Message("me","ta","ok. i'll just gonna cut me-self"));

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                AllCoursesActivity.SHARED_PREF_FILE_NAME , Context.MODE_PRIVATE
        );
        String student_email_id=sharedPreferences.getString(AllCoursesActivity.EMAIL_ID_EXTRA,"user");

        //code to add message UI
        for(com.example.kushagra.meetupapp.Message m : messArr)
        {
            View view;


            if(m.getSender().equals(student_email_id))
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


        file = new File(getApplicationContext().getFilesDir(),file_name);
        ArrayList<Query> Querarr = readQueryFile(file);



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

    }

    ArrayList<Query> readQueryFile(File file)
    {
        ObjectInputStream ois = null;

        try
        {
            ois = new ObjectInputStream(new FileInputStream(file));// input the read file.
            Log.d("FILETAG","Object Input stream opened...");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Log.d("FILETAG","Object Input did not stream opened...");
        }

        ArrayList<Query> Querarr = new ArrayList<Query>();

        try {
            Querarr = (ArrayList<Query>) ois.readObject() ;
            Log.d("FILE_FUNC","File read of size "+Querarr.size());
        } catch (ClassNotFoundException e) {
            Log.d("FILE_FUNC","File read me Class not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("FILE_FUNC","File read me IO");
            e.printStackTrace();
        } catch (NullPointerException n)
        {
            n.printStackTrace();
            Log.d("FILE_FUNC","File read me null pointer");
        }

        try {
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException n)
        {
            n.printStackTrace();
        }
        return Querarr;
    }





}
