package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.birdsofafeather.models.IStudent;
import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.StudentWithCourses;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        // Retrieve Student from database to put on ProfileView
        int test_id = 2;
        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        //TODO: Replace test_id with an intent from classmate list activity during linking task
        StudentWithCourses student = db.studentWithCoursesDao().get(test_id);

        // Set profile name
        TextView nameView = findViewById(R.id.name_view);
        nameView.setText(student.getName());

        // Retrieve profile image from URL using Picasso
        ImageView picture_view = (ImageView)findViewById(R.id.profile_picture_view);
        String url = student.getHeadshotURL();
        Picasso.get().load(url).into(picture_view);
        // Tag the image with its URL
        picture_view.setTag(url);

        // Compare other student with user's classes
        // The user is always the first entry in the database, so we use id 1
        StudentWithCourses me = db.studentWithCoursesDao().get(1);
        List<String> cc = student.getCommonCourses(me);
        String displayList = "";
        for (String course : cc){
            displayList = displayList + course;
            displayList = displayList + "\n";
        }
        TextView common_courses = findViewById(R.id.common_classes_view);
        common_courses.setText(displayList);
        common_courses.setVisibility(View.VISIBLE);
    }
}