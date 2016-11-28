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
import com.example.kushagra.meetupapp.network.api.ServerApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentFollowUpQueryActivity extends AppCompatActivity {

    private EditText chatBox;
    private LinearLayout msg_list;
    private File file;

    private String my_emailId;

    private Query globalCurrentQuery = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_follow_up_query);
        chatBox =(EditText)findViewById(R.id.message);
        msg_list = (LinearLayout)findViewById(R.id.msg_list);

        DbManipulate dbman=new DbManipulate(getApplicationContext());

        ArrayList<Query> Querarr;
        String courseId_file_name = getIntent().getStringExtra(AllCoursesActivity.COURSE_ID_EXTRA );

        if(getIntent().getBooleanExtra(AllCoursesActivity.IS_TA_SELECTED_EXTRA , false))
        {
            Querarr = dbman.getAllTAQueries(courseId_file_name);

        }
        else
        {
            file = new File(getApplicationContext().getFilesDir(), courseId_file_name);
            Querarr = readQueryFile(file);

        }

        String reqQueryId = getIntent().getStringExtra(AllCoursesActivity.RECYCLER_VIEW_QUERY_ID_EXTRA);

        for( Query q : Querarr)
        {
            if(q.getQueryId().equalsIgnoreCase(reqQueryId))
            {
                globalCurrentQuery = q;
            }
        }
        if( globalCurrentQuery ==null)
        {
            Log.d(MainActivity.TAG , "Entry NOT Created in Queries");

        }

        Log.d(MainActivity.TAG , "Quearr size" + Querarr.size() + " currentQuery = " + globalCurrentQuery.getTitle() );


//        ArrayList<Message> messArr=globalCurrentQuery.getMessages();


        //used this code to add following shit messages for testing. pleasee remove


        ArrayList<Message> messArr;
        messArr=dbman.getAllMessagesOfQueryId(globalCurrentQuery.getQueryId());

        Log.d(MainActivity.TAG , "Size of All  Messages " + messArr.size() );


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                AllCoursesActivity.SHARED_PREF_FILE_NAME , Context.MODE_PRIVATE
        );
        my_emailId = sharedPreferences.getString(AllCoursesActivity.EMAIL_ID_EXTRA,"user");

        //code to add message UI
        for(com.example.kushagra.meetupapp.Message msgObject : messArr)
            insertOneEntryIntoBalloonList(msgObject);
    }

    private void insertOneEntryIntoBalloonList(Message msgObject)
    {
        View view;


        if(msgObject.getSender().equalsIgnoreCase(my_emailId))
        {
            view = getLayoutInflater().inflate(R.layout.msg_balloon_me,null);
        }
        else
        {
            view = getLayoutInflater().inflate(R.layout.msg_balloon_them,null);
        }
        TextView t = (TextView)view.findViewById(R.id.msg_text);
        t.setText(msgObject.getMessage());
        msg_list.addView(view);

    }

    public void clickSendMessage(View v) throws IOException, ClassNotFoundException
    {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AllCoursesActivity.IP_ADD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApi service = retrofit.create(ServerApi.class);

        SharedPreferences sp = getApplicationContext()
                .getSharedPreferences(AllCoursesActivity.SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);



        final String my_emailId = sp.getString(AllCoursesActivity.EMAIL_ID_EXTRA, "default@email.com");
        String receiver_emailId = invertStudentTa( my_emailId );
        String message = chatBox.getText().toString();
        chatBox.setText("");


        Log.d(AllCoursesActivity.TAG, "sender emailId" + my_emailId + "receive email" + globalCurrentQuery.getTaId());


        final Message toadd = new Message(
                my_emailId,
                receiver_emailId,
                message,
                globalCurrentQuery.getQueryId()
        );



        insertOneEntryIntoBalloonList(toadd);


        Call<Message> call = service.sendMessage(toadd);
        call.enqueue(new Callback<Message>()
        {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response)
            {
                Log.d(MainActivity.TAG ," Query done Response");
                if(response.body()!=null)
                {
                    Log.d(MainActivity.TAG, "non null reposnce for sending message");



                    DbManipulate dbman = new DbManipulate(getApplicationContext());
                    dbman.insertMessageOfQuery(toadd, globalCurrentQuery.getQueryId());



                    /*

                   NEeD to update UI
                     */

                }
                else{
                    Log.d(MainActivity.TAG,"null respons on sending messages to server");
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.d(MainActivity.TAG, "failure to send message");
            }

            //check wheher posotion sender 0 or 1

//        globalCurrentQuery.getMessages().add(toadd);
//        Querarr.set(position,globalCurrentQuery);

        //write the Querarr

//        out.writeObject(Querarr);

        });


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

    private String invertStudentTa(String my_emailId)
    {
        if(globalCurrentQuery.getTaId().equalsIgnoreCase(my_emailId))
        {
            return globalCurrentQuery.getStudentId();

        }
        else
        {
            return globalCurrentQuery.getTaId();

        }
    }




}
