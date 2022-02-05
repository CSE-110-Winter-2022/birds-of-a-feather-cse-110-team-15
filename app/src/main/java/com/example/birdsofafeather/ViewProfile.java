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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);


        // Retrieve Student from database to put on ProfileView
        int test_id = 1;
        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        StudentWithCourses student = db.studentWithCoursesDao().get(test_id);

        String s = student.getName();

        TextView nameView = findViewById(R.id.name_view);
        nameView.setText(s);

        // Retrieve profile image from URL
        try {
            ImageView i = (ImageView)findViewById(R.id.profile_picture_view);
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL("https://cdn.britannica.com/18/137318-050-29F7072E/rooster-Rhode-Island-Red-roosters-chicken-domestication.jpg").getContent());
            i.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        StudentWithCourses me = db.studentWithCoursesDao().get(2);
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