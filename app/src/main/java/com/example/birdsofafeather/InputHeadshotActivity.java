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
    private boolean hasAlerted;
    //if the user pressed save without pasting any URL
    private String defaultURL = "https://lh3.googleusercontent.com/pw/AM-JKLUTkMaSnWQDXiRUw7FdrFk7luoo6VSJqafn8K1Bh1QksFJiO1oOjV5EoUbWnHc7xKtxDGeD9l8R6a7xtdfMFu4iz2y6QovxF0n4e3hZNGcq1izg_XLtUlX-BStPmG1FnGj9VW0wwoOy5G-i4VaNPA9I=s800-no?authuser=0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_headshot);

        // find place to set image
        profile = findViewById(R.id.profile);
        //find the URL
        editURL = findViewById(R.id.editURL);
        // show default profile image
        showHeadshot(defaultURL);

        //get the intent
        Bundle extras = getIntent().getExtras();
        name = extras.getString("student_name");
    }

    public void showHeadshot(String URL) {
        // TODO: need to find better way to check valid URL before displaying
        try {
            Picasso.get().load(URL).into(profile);
        } catch (Exception e) {
            Picasso.get().load(defaultURL).into(profile);
            Utilities.showAlert(this, "Invalid URL");
        }
    }

    //https://guides.codepath.com/android/Displaying-Images-with-the-Picasso-Library
    public void onSaveClick(View view) {
        String URL = editURL.getText().toString();
        showHeadshot(URL);
    }


    public void onContinueClick(View view) {
        //save student to database
        editURL = findViewById(R.id.editURL);
        String enteredURL = editURL.getText().toString();
        // alert them once to warn them default profile will be used if no URL entered
        if(enteredURL.length() == 0 && !hasAlerted) {
            Utilities.showAlert(this, "No headshot provided, will use default photo as shown. If that is okay, press continue again.");
            hasAlerted = !hasAlerted;
            return;
        }
        String URL = enteredURL.length() == 0 ? defaultURL : enteredURL;
        //write the student to database
        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        Student s1 = new Student(name, URL);
        db.studentWithCoursesDao().insert(s1);

        //get the intent
        Intent intent = new Intent(this, EnterCourseActivity.class);
        // pass newly created student_id
        intent.putExtra("student_id", db.studentWithCoursesDao().lastIdCreated());
        startActivity(intent);
        finish();
    }
}
