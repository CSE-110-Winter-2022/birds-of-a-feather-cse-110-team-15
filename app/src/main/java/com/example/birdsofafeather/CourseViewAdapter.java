package com.example.birdsofafeather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CourseViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> courses;

    public CourseViewAdapter(Context context, ArrayList<String> courses){
        this.context = context;
        this.courses = courses;
    }

    @Override
    public int getCount(){
        return this.courses.size();
    }

    @Override
    public String getItem(int position){
        return this.courses.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.course_row, parent, false);

        TextView courseView = (TextView) view.findViewById(R.id.course_row_text);
        courseView.setText(courses.get(position));

        Button deleteButton = (Button) view.findViewById(R.id.remove_course_button);
        deleteButton.setTag(position);

        deleteButton.setOnClickListener( (view1) ->{
            courses.remove(position);
            notifyDataSetChanged();
                }
        );

        return view;
    }
}
