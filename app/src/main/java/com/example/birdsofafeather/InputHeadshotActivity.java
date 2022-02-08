package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Student;
import com.squareup.picasso.Picasso;

public class InputHeadshotActivity extends AppCompatActivity{
    EditText editURL;
    ImageView profile;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_headshot);

        //find the URL
        editURL = findViewById(R.id.editURL);

        //get the intent
        Bundle extras = getIntent().getExtras();
        name = extras.getString("student_name");
    }

    //https://guides.codepath.com/android/Displaying-Images-with-the-Picasso-Library
    public void onSaveClick(View view) {
        profile = findViewById(R.id.profile);
        String URL = editURL.getText().toString();
        Picasso.get().load(URL)
                .into(profile);
    }


    public void onContinueClick(View view) {
        //save student to database
        editURL = findViewById(R.id.editURL);
        String URL = editURL.getText().toString();
        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        Student s1 = new Student(db.studentWithCoursesDao().count()+1, name, URL);
        db.studentWithCoursesDao().add(s1);

        //get the intent
        Intent intent = new Intent(this, EnterCourseActivity.class);
        startActivity(intent);
        finish();
    }
}
