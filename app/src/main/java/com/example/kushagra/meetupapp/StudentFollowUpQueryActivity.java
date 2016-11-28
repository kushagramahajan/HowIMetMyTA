package com.example.kushagra.meetupapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

    int day, month, year, hour, minute;
    String meetingvenue;


    Boolean flag;
    private EditText chatBox;
    private LinearLayout msg_list;
    private File file;
    TextView title,text;
    ImageButton send,meet;
    DbManipulate dbman;

    public static final String REFRESH_CHAT_UI_INTENT = "com.example.kushagra.REFRESH_CHAT_UI_INTENT";

    public static final String QUERY_ID_BROADCAST_RECV = "com.example.kushagra.broadcastrecv";


    private String my_emailId;

    private Query globalCurrentQuery = null ;
    private DataUpdateReceiver dataUpdateReceiver;

    @Override
    protected void onPause()
    {
        super.onPause();
        if (dataUpdateReceiver != null)
            unregisterReceiver(dataUpdateReceiver);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (dataUpdateReceiver == null)
            dataUpdateReceiver = new DataUpdateReceiver();

        IntentFilter intentFilter = new IntentFilter(REFRESH_CHAT_UI_INTENT);
        registerReceiver(dataUpdateReceiver, intentFilter);
    }

    private void insertEveryMessageIntoLinearLayout()
    {

        ArrayList<Message> messArr;
        messArr = dbman.getAllMessagesOfQueryId(globalCurrentQuery.getQueryId());

        Log.d(MainActivity.TAG , "Size of All  Messages " + messArr.size() );


        for(com.example.kushagra.meetupapp.Message msgObject : messArr)
        {
            if(msgObject.getMessage().contains("TIME~"))
            {
                String[] arr = msgObject.getMessage().split("~");
                day = Integer.parseInt(arr[1]);
                month = Integer.parseInt(arr[2]);
                year = Integer.parseInt(arr[3]);
                hour = Integer.parseInt(arr[4]);
                minute = Integer.parseInt(arr[5]);
                SharedPreferences.Editor editor = getSharedPreferences("MySharedPreference", MODE_PRIVATE).edit();
                String qid = getIntent().getStringExtra(AllCoursesActivity.RECYCLER_VIEW_QUERY_ID_EXTRA);
                editor.putBoolean(qid, true);
                editor.apply();

                chatBox.setVisibility(View.GONE);
                send.setVisibility(View.GONE);
                meet.setVisibility(View.GONE);

                String s = null;
                if(getIntent().getBooleanExtra(AllCoursesActivity.IS_TA_SELECTED_EXTRA , false))
                {
                    s = "You have fixed a meeting with "+ invertStudentTa(my_emailId)+ " on "+ day+"/"+
                            month + "/" + year + " at " + hour + ":" + minute;
                }
                else
                {
                    s = invertStudentTa(my_emailId)+" has fixed a meeting with you on "+ day + "/" +
                            month + "/" + year + " at " + hour + ":" + minute;
                }

                //dbman.insertMessageOfQuery(new Message("neutral","neutral",s,qid),qid);

                insertOneEntryIntoBalloonList(new Message("neutral","neutral",s,qid));

                break;
            }
            insertOneEntryIntoBalloonList(msgObject);

        }
    }
    private void refreshChatUI(String queryId)
    {

        if(globalCurrentQuery.getQueryId().equalsIgnoreCase(queryId))
        {
            if(((LinearLayout) msg_list).getChildCount() > 0)
            {
                ((LinearLayout) msg_list).removeAllViews();

                insertEveryMessageIntoLinearLayout();

            }




        }


    }
    private class DataUpdateReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(REFRESH_CHAT_UI_INTENT))
            {
                Log.d(MainActivity.TAG , "From Receiver" + intent.getStringExtra(QUERY_ID_BROADCAST_RECV));
                refreshChatUI(intent.getStringExtra( QUERY_ID_BROADCAST_RECV ));

                // Do stuff - maybe update my view based on the changed DB contents
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_follow_up_query);
        chatBox =(EditText)findViewById(R.id.message);
        msg_list = (LinearLayout)findViewById(R.id.msg_list);
        send = (ImageButton) findViewById(R.id.send);
        meet = (ImageButton) findViewById(R.id.meet);
        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.text);
        SharedPreferences sp = getSharedPreferences("MySharedPreference",MODE_PRIVATE);
        Boolean status = sp.getBoolean(getIntent().getStringExtra(AllCoursesActivity.RECYCLER_VIEW_QUERY_ID_EXTRA),false);

        if(status)
        {
            chatBox.setVisibility(View.GONE);
            send.setVisibility(View.GONE);
            meet.setVisibility(View.GONE);
        }

        dbman=new DbManipulate(getApplicationContext());

        ArrayList<Query> Querarr;
        String courseId_file_name = getIntent().getStringExtra(AllCoursesActivity.COURSE_ID_EXTRA );

        if(getIntent().getBooleanExtra(AllCoursesActivity.IS_DESCREPANCY_EXTRA , false))
        {
            Querarr = dbman.getAllTAQueries(courseId_file_name);
            if(Querarr.size() == 0)
            {
                file = new File(getApplicationContext().getFilesDir(), courseId_file_name);
                Querarr = readQueryFile(file);
            }
        }
        else if(getIntent().getBooleanExtra(AllCoursesActivity.IS_TA_SELECTED_EXTRA , false))
        {
            Querarr = dbman.getAllTAQueries(courseId_file_name);

        }
        else
        {
            file = new File(getApplicationContext().getFilesDir(), courseId_file_name);
            Querarr = readQueryFile(file);
            meet.setVisibility(View.GONE);
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

        Log.d(MainActivity.TAG , "Quearr size" + Querarr.size() + " currentQuery = " + globalCurrentQuery.getTitle() +
        globalCurrentQuery.getStudentId() + " ta =" + globalCurrentQuery.getTaId() );


//        ArrayList<Message> messArr=globalCurrentQuery.getMessages();


        //used this code to add following shit messages for testing. pleasee remove

        title.setText(globalCurrentQuery.getTitle());
        text.setText(globalCurrentQuery.getDescription());
        getSupportActionBar().setTitle(globalCurrentQuery.getTitle());


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                AllCoursesActivity.SHARED_PREF_FILE_NAME , Context.MODE_PRIVATE
        );
        my_emailId = sharedPreferences.getString(AllCoursesActivity.EMAIL_ID_EXTRA,"user");


        insertEveryMessageIntoLinearLayout();
    }

    private void insertOneEntryIntoBalloonList(Message msgObject)
    {
        View view;
        if(msgObject.getSender().equalsIgnoreCase("neutral"))
        {
            view = getLayoutInflater().inflate(R.layout.msg_balloon_neutral,null);

        }
        else if(msgObject.getSender().equalsIgnoreCase(my_emailId))
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
        sendAction("doNot");
    }

    public void sendAction(String mark)
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

        if(mark.equals("doIt"))
        {
            message = "TIME~"+day+"~"+month+"~"+year+"~"+hour+"~"+minute;

        }

        Log.d(AllCoursesActivity.TAG, "sender emailId" + my_emailId + "receive email" + receiver_emailId);


        final Message toadd = new Message(
                my_emailId,
                receiver_emailId,
                message,
                globalCurrentQuery.getQueryId()
        );

        if(!mark.equals("doIt"))
        {
            insertOneEntryIntoBalloonList(toadd);
        }


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

    public void onMeetClicked(View view)
    {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(StudentFollowUpQueryActivity.this);
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

                String qid = getIntent().getStringExtra(AllCoursesActivity.RECYCLER_VIEW_QUERY_ID_EXTRA);
                SharedPreferences.Editor editor = getSharedPreferences("MySharedPreference", MODE_PRIVATE).edit();
                editor.putBoolean(qid, true);
                editor.commit();

                chatBox.setVisibility(View.GONE);
                send.setVisibility(View.GONE);
                meet.setVisibility(View.GONE);

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

                    View view2 = getLayoutInflater().inflate(R.layout.msg_balloon_neutral,null);
                    TextView t = (TextView)view2.findViewById(R.id.msg_text);
                    String s = null;
                    if(getIntent().getBooleanExtra(AllCoursesActivity.IS_TA_SELECTED_EXTRA , false))
                    {
                        s = "You have fixed a meeting with "+ invertStudentTa(my_emailId)+ " on "+ day+"/"+
                                month + "/" + year + " at " + hour + ":" + minute;
                    }
                    else
                    {
                        s = invertStudentTa(my_emailId)+" has fixed a meeting with you on "+ day + "/" +
                                month + "/" + year + " at " + hour + ":" + minute;
                    }
                    t.setText(s);
                    msg_list.addView(view2);
                    //dbman.insertMessageOfQuery(new Message("neutral","neutral",s,qid),qid);

                    sendAction("doIt");

                    alertDialog.cancel();
                }
            }
        });
        button_discard.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                flag=false;
                alertDialog.cancel();
            }
        });
        flag=true;
        alertDialog.show();
    }


}
