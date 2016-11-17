package com.example.kushagra.meetupapp.navDrawer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.kushagra.meetupapp.R;
import com.example.kushagra.meetupapp.db.objects.Course;
import com.example.kushagra.meetupapp.navDrawer.recyclerView.ClickListener;
import com.example.kushagra.meetupapp.navDrawer.recyclerView.CommonCoursesAdapter;
import com.example.kushagra.meetupapp.navDrawer.recyclerView.DividerItemDecoration;
import com.example.kushagra.meetupapp.navDrawer.recyclerView.RecyclerTouchListener;

import java.util.ArrayList;

public class CommonCoursesListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private RecyclerView recyclerView;
    private CommonCoursesAdapter mAdapter;
    private ArrayList<Course> commonCoursesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_queries_list_common);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        handleRecyclerView();
        Log.d("TAG" , "asdas");
    }


    private void handleRecyclerView()
    {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_common_list);

        mAdapter = new CommonCoursesAdapter( commonCoursesList , getApplicationContext() );

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        // set the adapter

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position)
            {


                Course course = commonCoursesList.get(position);
                Toast.makeText(getApplicationContext(), course.getCourseId() + " is selected!", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


       // commonCoursesList = new ArrayList<>();

        commonCoursesList.add(new Course("def TA Name" , "def001" , null));



        mAdapter.notifyDataSetChanged();
        recyclerView.invalidate();


    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.courses_list_common, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_ta_side)
        {
            commonCoursesList.clear();

            commonCoursesList.add(new Course("TA Name" , "def001" , null));
            commonCoursesList.add(new Course("TA Name" , "def001" , null));


            mAdapter.notifyDataSetChanged();

            // Handle the camera action
        }
        else if (id == R.id.nav_student_side)
        {

            commonCoursesList.clear();

            commonCoursesList.add(new Course("Student Name" , "def002" , null));
            commonCoursesList.add(new Course("Student Name" , "def002" , null));

            mAdapter.notifyDataSetChanged();



        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
