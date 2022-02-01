package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        // TODO: Convert this temporary placeholders to calls to Room API
        String s = "Ava";

        TextView nameView = findViewById(R.id.name_view);
        nameView.setText(s);

        ImageView profile_pic_view = findViewById(R.id.profile_picture_view);

        // TODO: Get this image from persisted data, remove sample image from drawable
        profile_pic_view.setImageResource(R.drawable.placeholder_profile_pic);


        // TODO: Get a list of shared classes, format them as strings.
        ArrayList<String> test_classes = new ArrayList<>(Arrays.asList("WI22 CSE 110", "FA19 CSE 12", "SP18 MATH 20A"));
        String displayList = "";
        for (String course : test_classes){
            displayList = displayList + course;
            displayList = displayList + "\n";
        }

        TextView common_courses = findViewById(R.id.common_classes_view);
        common_courses.setText(displayList);
        common_courses.setVisibility(View.VISIBLE);
    }
}