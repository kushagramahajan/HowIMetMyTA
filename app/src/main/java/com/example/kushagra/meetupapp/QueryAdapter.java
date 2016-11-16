package com.example.kushagra.meetupapp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Prankul on 27-10-2016.
 */

import java.util.ArrayList;

/**
 * Created by Prankul on 16-10-2016.
 */

public class QueryAdapter extends RecyclerView.Adapter
{

    private ArrayList<Query> list;
    private String currentCoutseId;

    public QueryAdapter(ArrayList<Query> list , String currentCourseId )
    {
        this.currentCoutseId = currentCourseId;
        this.list = list;
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView title,subtitle;
        private Query item;

        public ItemHolder(final View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.course);
            subtitle = (TextView) itemView.findViewById(R.id.prof);
            itemView.setOnClickListener(this);
        }

        public void bindItem(Query item)
        {
            this.item = item;
            title.setText(item.getTitle());
            subtitle.setText(item.getQuery());
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(itemView.getContext(),"Card Clicked - " + getAdapterPosition(),Toast.LENGTH_SHORT).show();
            Intent i = new Intent(v.getContext(),StudentFollowUpQueryActivity.class);

            i.putExtra("position", getAdapterPosition());
            i.putExtra( AllCoursesActivity.COURSE_ID_EXTRA , currentCoutseId );
            v.getContext().startActivity(i);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tile, parent, false);
        return new ItemHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Query titem = list.get(position);
        ((ItemHolder)holder).bindItem(titem);
    }

    @Override
    public int getItemCount() {
        if(list==null)
            return 0;
        return list.size();
    }
}
