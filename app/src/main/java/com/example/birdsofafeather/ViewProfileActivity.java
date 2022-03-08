package com.example.birdsofafeather;

<<<<<<< Updated upstream
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
=======
>>>>>>> Stashed changes
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.StudentWithCourses;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewProfileActivity extends AppCompatActivity {

    private final BoFServiceConnection serviceConnection = new BoFServiceConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Intent intent = new Intent(this, NearbyBackgroundService.class);
        intent.putExtra("uuid", new UUIDManager(getApplicationContext()).getUserUUID());
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        isBound = true;

        // Retrieve Student from database to put on ProfileView
        Bundle extras = getIntent().getExtras();
        String classmate_id = extras.getString("classmate_id");
        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        StudentWithCourses student = db.studentWithCoursesDao().get(classmate_id);

        // Set profile name
        TextView nameView = findViewById(R.id.name_view);
        nameView.setText(student.getName());

        Picasso picasso = new Picasso.Builder(this).build();
        try {
            Picasso.setSingletonInstance(picasso);
        } catch (Exception e) {
        }

        CheckBox favoriteCheck =  findViewById(R.id.profile_favorite);
        // Set favorite icon
        favoriteCheck.setChecked(student.isFavorite());
        favoriteCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (buttonView.isChecked()) {
                        Toast.makeText(ViewProfileActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                        db.studentWithCoursesDao().updateFavorite(student.getUUID(), true);

                    } else {
                        Toast.makeText(ViewProfileActivity.this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                        db.studentWithCoursesDao().updateFavorite(student.getUUID(), false);
                    }
                }
        );

        //send wave to student
        CheckBox waveCheck = findViewById(R.id.send_wave);
        waveCheck.setChecked(student.getWavedFromUser());
        waveCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (buttonView.isChecked()) {
                        Toast.makeText(ViewProfileActivity.this, "Send Wave", Toast.LENGTH_SHORT).show();
                        db.studentWithCoursesDao().updateWaveFrom(student.getUUID(), true);
                        student.setWavedFromUser(true);

                        String userUUID = new UUIDManager(getApplicationContext()).getUserUUID();
                        String studentInfo = student.getUUID();

                        //send wave by publishing message
                        NearbyBackgroundService nearbyService = serviceConnection.getNearbyService();
                        nearbyService.publish(waveMessage(student));

                        //disable checkbox
                       // waveCheck.setEnabled(false);
                    }
                }
        );

        // Retrieve profile image from URL using Picasso
        ImageView picture_view = (ImageView)findViewById(R.id.profile_picture_view);
        String url = student.getHeadshotURL();
        Picasso.get().load(url).into(picture_view);
        // Tag the image with its URL
        picture_view.setTag(url);

        // Compare other student with user's classes
        // The user is always the first entry in the database, so we use id 1
        String currentUserID = new UUIDManager(getApplicationContext()).getUserUUID();

        StudentWithCourses me = db.studentWithCoursesDao().get(currentUserID);
        List<String> cc = student.getCommonCourses(me);
        StringBuilder displayList = new StringBuilder();
        for (String course : cc){
            displayList.append(course);
            displayList.append("\n");
        }
        TextView common_courses = findViewById(R.id.common_classes_view);
        common_courses.setText(displayList.toString());
        common_courses.setVisibility(View.VISIBLE);
    }

    protected String waveMessage(StudentWithCourses student){
        //obtain strings to input into service
      //  String userUUID = new UUIDManager(getApplicationContext()).getUserUUID();
        String studentInfo = student.getUUID() + ",,,,\n" +
                student.getName() + ",,,,\n" +
                student.getHeadshotURL() + ",,,,\n";
        for(String course: student.getCourses()){
            String temp = course.replace(" ", ",");
            studentInfo += temp + ",\n";
        }
        studentInfo += student.getUUID() + ",wave,,,";

        return studentInfo;
    }

    // make sure to stop service when ending activity
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceConnection.isBound())  {
            unbindService(serviceConnection);
            serviceConnection.setBound(false);
        }
    }
}