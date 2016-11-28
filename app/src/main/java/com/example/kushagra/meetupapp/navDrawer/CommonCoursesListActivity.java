package com.example.kushagra.meetupapp.navDrawer;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kushagra.meetupapp.AllCoursesActivity;
import com.example.kushagra.meetupapp.MainActivity;
import com.example.kushagra.meetupapp.Query;
import com.example.kushagra.meetupapp.R;
import com.example.kushagra.meetupapp.StartNewStudentQueryActivity;
import com.example.kushagra.meetupapp.StudentFollowUpQueryActivity;
import com.example.kushagra.meetupapp.db.DbManipulate;
import com.example.kushagra.meetupapp.db.objects.Course;
import com.example.kushagra.meetupapp.navDrawer.recyclerView.ClickListener;
import com.example.kushagra.meetupapp.navDrawer.recyclerView.CommonAdapter;
import com.example.kushagra.meetupapp.navDrawer.recyclerView.CommonCoursesAdapter;
import com.example.kushagra.meetupapp.navDrawer.recyclerView.CommonQueryAdapter;
import com.example.kushagra.meetupapp.navDrawer.recyclerView.RecyclerTouchListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class CommonCoursesListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private RecyclerView recyclerView;
    public static CommonAdapter mAdapter;

    private ArrayList<Course> commonCoursesList = new ArrayList<>();
    public static ArrayList<Query> commonQueryList = new ArrayList<>();


    private static boolean isTaSelected = false;
    private static boolean isInsideQuerySide_ofCourse = false;

    DbManipulate dbManipulate;
    FloatingActionButton newQueryFabButton;
    private static boolean somethingSelected = false;





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d(MainActivity.TAG , "Inside Target");

        dbManipulate = new DbManipulate(getApplicationContext());



        setContentView(R.layout.activity_queries_list_common);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        newQueryFabButton = (FloatingActionButton) findViewById(R.id.fab);
        newQueryFabButton.hide();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        isTaSelected = false;
        isInsideQuerySide_ofCourse = false;

        handleCourseSideRecyclerView();

        loadUserImageFromPrivateExternalStorage(getApplicationContext());

    }

    private void reDrawRecyclerView()
    {
        try
        {
            recyclerView.setAdapter(null);
            recyclerView.setLayoutManager(null);
            recyclerView.setOnClickListener(null);
        }
        catch (NullPointerException ne)
        {
            ne.printStackTrace();
        }

    }

    private void handleCourseSideRecyclerView()
    {
        newQueryFabButton.hide();

        reDrawRecyclerView();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_common_list);

        mAdapter = new CommonCoursesAdapter( commonCoursesList , getApplicationContext() );

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
      //  recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter((CommonCoursesAdapter)mAdapter);

        // set the adapter

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position)
            {

                if(!somethingSelected) {


                    Course currentCourse = commonCoursesList.get(position);
                    Toast.makeText(getApplicationContext(), currentCourse.getCourseId() + " Course is selected!",
                            Toast.LENGTH_SHORT).show();

                    handleQuerySideRecyclerView(currentCourse);

                    //Start Student Query ACtivity

                    //TA Query Act
                somethingSelected = false;
                }

            }

            @Override
            public void onLongClick(View view, int position)
            {

            }
        }));

        setAndPopulateStudentCourses();

    }




    private void handleQuerySideRecyclerView(final Course currentCourse)
    {

        if(!isTaSelected)
        {
            newQueryFabButton.show();

            newQueryFabButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(getApplicationContext(), StartNewStudentQueryActivity.class);

                    Log.d(MainActivity.TAG, "Name" + currentCourse.getCourseName()
                            + "Id" + currentCourse.getCourseId() );

                    i.putExtra(AllCoursesActivity.COURSE_NAME_EXTRA,
                            currentCourse.getCourseName());

                    i.putExtra(AllCoursesActivity.COURSE_ID_EXTRA,
                            currentCourse.getCourseId());

                    startActivity(i);


                }
            });
        }
        else
        {
            newQueryFabButton.hide();

        }

        reDrawRecyclerView();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_common_list);

        mAdapter = new CommonQueryAdapter( commonQueryList , getApplicationContext() );

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
      //  recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter((CommonQueryAdapter)mAdapter);

        // set the adapter

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position)
            {
                if(!somethingSelected) {


                    Query query = commonQueryList.get(position);
                    Toast.makeText(CommonCoursesListActivity.this, query.getQueryId() + " q is selected!", Toast.LENGTH_SHORT).show();


                    Intent i = new Intent(CommonCoursesListActivity.this, StudentFollowUpQueryActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);


                    i.putExtra(AllCoursesActivity.RECYCLER_VIEW_QUERY_ID_EXTRA, query.getQueryId());
                    i.putExtra(AllCoursesActivity.IS_TA_SELECTED_EXTRA, isTaSelected);
                    i.putExtra(AllCoursesActivity.COURSE_ID_EXTRA, currentCourse.getCourseId());

                    startActivity(i);


                    somethingSelected = true;
                }

                //Start Student Query ACtivity
                //TA Query Act


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        //Add Student Side Querys

        commonQueryList.clear();
//        public Query(String qid, String title, String description, String taId, String studentId) {







        if(isTaSelected)
        {
            commonQueryList.clear();

            Log.d(AllCoursesActivity.TAG , "Reading All Queries "  + currentCourse.getCourseId() + " = " +

                    dbManipulate.getAllTAQueries(currentCourse.getCourseId()).size() );

            commonQueryList.addAll( dbManipulate.getAllTAQueries(currentCourse.getCourseId()) );

            ((CommonQueryAdapter) mAdapter).notifyDataSetChanged();

            recyclerView.invalidate();

        }
        else
        {

            String file_name = currentCourse.getCourseId();

            Log.d(AllCoursesActivity.TAG , "Student Course Id" + file_name );

            File file = new File(getApplicationContext().getFilesDir(), file_name);

            ArrayList<Query> Querarr = readQueryFile(file);

            commonQueryList.clear();
            commonQueryList.addAll(Querarr);


            ((CommonQueryAdapter) mAdapter).notifyDataSetChanged();
            recyclerView.invalidate();

        }


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

        handleCourseSideRecyclerView();


        if (id == R.id.nav_ta_side)
        {

            setAndPopulateTACourses();
        }
        else if (id == R.id.nav_student_side)
        {
            setAndPopulateStudentCourses();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setAndPopulateStudentCourses()
    {
        isTaSelected = false;



        commonCoursesList.clear();

     //   commonCoursesList.add(new Course("Student crsc Dummy" , "def002" , null));

        Log.d(AllCoursesActivity.TAG , "Courses  Size  = " +dbManipulate.getMyCourses().size() );

        commonCoursesList.addAll(
                dbManipulate.getMyCourses()
        );

        ((CommonCoursesAdapter)mAdapter).notifyDataSetChanged();
        recyclerView.invalidate();


    }

    private void setAndPopulateTACourses()
    {



        commonCoursesList.clear();
        isTaSelected = true;

        /*
I AM TA OF MY COURSES */


        ArrayList<Course> edmo = new ArrayList<>();
        edmo.add( dbManipulate.getMyCourses().get(0) );

        Log.d(AllCoursesActivity.TAG , "edmo size" + edmo.size() );

        /*............*/

        commonCoursesList.addAll(
        edmo
        );

        ((CommonCoursesAdapter)mAdapter).notifyDataSetChanged();
        recyclerView.invalidate();

    }




    ArrayList<Query> readQueryFile(File file)
    {
        ObjectInputStream ois = null;

        try
        {
            ois = new ObjectInputStream(new FileInputStream(file));// input the read file.
            Log.d("FILE_FUNC","Object Input stream opened...");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Log.d("FILE_FUNC","Object Input did not stream opened...");
        }

        ArrayList<Query> Querarr = new ArrayList<Query>();

        try
        {
            Querarr = (ArrayList<Query>) ois.readObject() ;
            Log.d("FILE_FUNC","File read of size "+Querarr.size());
        }
        catch (ClassNotFoundException e)
        {
            Log.d("FILE_FUNC","File read me Class not found");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            Log.d("FILE_FUNC","File read me IO");
            e.printStackTrace();
        }
        catch (NullPointerException n)
        {
            n.printStackTrace();
            Log.d("FILE_FUNC","File read me null pointer");
        }

        try
        {
            ois.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException n)
        {
            n.printStackTrace();
        }

        return Querarr;
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        somethingSelected = false;
    }



    private void loadUserImageFromPrivateExternalStorage(Context context)
    {
        File folder = getPrivateAlbumStorageDir(context);
        String path =  folder + AllCoursesActivity.PROFILE_IMAGE_FILE_NAME;

        if(Drawable.createFromPath(path)!=null)
        {
            ImageView profilePic = (ImageView) findViewById(R.id.imageView_UserProfilePic);
            profilePic.setImageDrawable(Drawable.createFromPath(path));

        }
        else
        {
            Log.d(MainActivity.TAG , "Image Private Failed");
        }

    }

    public File getPrivateAlbumStorageDir(Context context)
    {
        // Get the directory for the app's private pictures directory.
        File folder = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), AllCoursesActivity.PROFILE_IMAGE_FILE_NAME);

        if((!folder.exists()) && (!folder.mkdirs()))
        {
            Log.e( MainActivity.TAG , "Directory not created");

        }

        return folder;
    }
}
