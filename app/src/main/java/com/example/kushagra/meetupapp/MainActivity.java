package com.example.kushagra.meetupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mLoginButton;
    public static final String TAG = "SP_Main";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoginButton = (Button) findViewById(R.id.LoginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d(TAG, "Clicked Login");
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });


    }

}
