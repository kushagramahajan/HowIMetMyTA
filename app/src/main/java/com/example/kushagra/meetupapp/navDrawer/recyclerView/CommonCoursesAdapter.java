package com.example.kushagra.meetupapp.navDrawer.recyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kushagra.meetupapp.R;
import com.example.kushagra.meetupapp.db.objects.Course;

import java.util.ArrayList;

/**
 * Created by Himanshu Sagar on 16-11-2016.
 */

public class CommonCoursesAdapter extends RecyclerView.Adapter< CommonCoursesAdapter.ViewHolder> {

    private ArrayList<Course> commonCoursesList;
    private Context mContext;

    public CommonCoursesAdapter(ArrayList<Course> commonQueriesList , Context mContext)
    {
        this.commonCoursesList = commonQueriesList;
        this.mContext = mContext;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView commonCourseId , commonCourseName;

        public ViewHolder(View view)
        {
            super(view);
            commonCourseId = (TextView) view.findViewById(R.id.common_courseId);
            commonCourseName= (TextView) view.findViewById(R.id.common_courseName);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.queries_list_common_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position)
    {
        Course currentCourse  = commonCoursesList.get(position);
        holder.commonCourseId.setText( currentCourse.getCourseId() );
        holder.commonCourseName.setText( currentCourse.getCourseName() );

    }

    @Override
    public int getItemCount() {
        return commonCoursesList.size();
    }
}