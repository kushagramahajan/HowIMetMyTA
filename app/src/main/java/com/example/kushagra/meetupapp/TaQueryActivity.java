package com.example.kushagra.meetupapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class TaQueryActivity extends AppCompatActivity {

    Context mContext;
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ta_query);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = getApplicationContext();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, StartNewTaQueryActivity.class);
                startActivity(i);
            }
        });


        list = (RecyclerView) findViewById(R.id.list);

        ArrayList<Query> myQueries = new ArrayList<>();

        QueryAdapter adapter = new QueryAdapter(myQueries);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager( mContext ));
    }

}