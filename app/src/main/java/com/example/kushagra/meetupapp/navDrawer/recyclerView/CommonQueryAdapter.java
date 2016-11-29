package com.example.kushagra.meetupapp.navDrawer.recyclerView;

/**
 * Created by Himanshu Sagar on 27-11-2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kushagra.meetupapp.Query;
import com.example.kushagra.meetupapp.R;

import java.util.ArrayList;


/**
 * Created by Himanshu Sagar on 16-11-2016.
 */

public class CommonQueryAdapter extends RecyclerView.Adapter< CommonQueryAdapter.ViewHolder>
        implements CommonAdapter
{


    private ArrayList<Query> commonQueryList;
    private Context mContext;

    public CommonQueryAdapter(ArrayList<Query> commonQueryList , Context mContext)
    {
        this.commonQueryList = commonQueryList;
        this.mContext = mContext;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView commonQueryTitle , commonQueryName;

        public ViewHolder(View view)
        {
            super(view);
            commonQueryTitle = (TextView) view.findViewById(R.id.common_id);
            commonQueryName= (TextView) view.findViewById(R.id.common_name);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tile_query, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position)
    {
        Query currentQuery  = commonQueryList.get(position);
        holder.commonQueryTitle.setText( currentQuery.getTitle() );
        holder.commonQueryName.setText( currentQuery.getDescription() );

    }

    @Override
    public int getItemCount() {
        return commonQueryList.size();
    }
}