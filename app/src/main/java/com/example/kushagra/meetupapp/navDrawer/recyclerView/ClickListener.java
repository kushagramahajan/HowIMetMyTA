package com.example.kushagra.meetupapp.navDrawer.recyclerView;

import android.view.View;

/**
 * Created by Himanshu Sagar on 05-11-2016.
 */

public interface ClickListener
{
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}