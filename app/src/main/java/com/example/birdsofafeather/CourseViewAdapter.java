package com.example.birdsofafeather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CourseViewAdapter extends BaseAdapter {
    private ArrayList<String> courses;

    public CourseViewAdapter(ArrayList<String> courses){
        super();
        this.courses = courses;
    }

    @NonNull
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
        View rowView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.course_row, parent, false);

        TextView courseView = (TextView) rowView.findViewById(R.id.course_row_text);
        courseView.setText(courses.get(position));

        Button deleteButton = (Button) rowView.findViewById(R.id.remove_course_button);

        deleteButton.setOnClickListener( (view1) ->{
            courses.remove(position);
            notifyDataSetChanged();
                }
        );

        return rowView;
    }
}
