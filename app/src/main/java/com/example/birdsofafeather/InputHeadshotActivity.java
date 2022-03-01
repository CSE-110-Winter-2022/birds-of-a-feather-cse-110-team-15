package com.example.birdsofafeather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Student;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class InputHeadshotActivity extends AppCompatActivity{
    EditText editURL;
    ImageView profile;
    private String name;
    private Picasso picasso;

    // Default profile picture to use as needed
    private String defaultURL = "https://lh3.googleusercontent.com/pw/AM-JKLUTkMaSnWQDXiRUw7Fdr" +
            "Fk7luoo6VSJqafn8K1Bh1QksFJiO1oOjV5EoUbWnHc7xKtxDGeD9l8R6a7xtdfMFu4iz2y6QovxF0n4e3hZ" +
            "NGcq1izg_XLtUlX-BStPmG1FnGj9VW0wwoOy5G-i4VaNPA9I=s800-no?authuser=0";
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_headshot);

        profile = findViewById(R.id.profile);
        editURL = findViewById(R.id.editURL);
        URL = editURL.getText().toString();

        // Get the intent from UsernameActivity
        Bundle extras = getIntent().getExtras();
        name = extras.getString("student_name");
    }

    public void loadHeadshot() {
        // Use Picasso to easily load images from URLs into ImageView
        // Docs: https://guides.codepath.com/android/Displaying-Images-with-the-Picasso-Library
        picasso = new Picasso.Builder(this).build();

        // Try catch block needed for testing only
        try {
            Picasso.setSingletonInstance(picasso);
        } catch (Exception e) {
        }

        // Load image into view
        Activity headshotActivity = this;
        picasso.get().load(URL).into(profile, new Callback() {
            @Override
            public void onSuccess() {
                return;
            }
            @Override
            public void onError(Exception e) {
                // Error handling for invalid URLs. (Use a default image and display alert)
                URL = defaultURL;
                editURL.setText("");
                Picasso.get().load(URL).into(profile);
                Utilities.showAlert(headshotActivity, "Invalid URL. Please reenter " +
                        "the URL or press Continue to proceed with default image.");
            }
        });
        profile.setTag(URL);
    }

    public void onSaveClick(View view) {
        String entered_url = editURL.getText().toString();
        // Check for empty URL, set to default if necessary
        if (entered_url.equals("")) {
            URL = defaultURL;
            Utilities.showAlert(this, "No headshot provided, Please enter an image" +
                    " URL or press Continue to proceed with default image.");
        } else {
            URL = entered_url;
        }
        loadHeadshot();
    }

    public void onContinueClick(View view) {
        // Check for empty URL, set to default if necessary. Slightly different behavior than Save.
        if (URL.equals("")) {
            URL = defaultURL;
            Utilities.showAlert(this, "No headshot provided, Please enter an image" +
                    " URL or press Continue to proceed with default image.");
            loadHeadshot();
            return;
        }
        loadHeadshot();

        // Write the student to database
        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        Student s1 = new Student(getIntent().getStringExtra("uuid"), name, URL);
        db.studentWithCoursesDao().insert(s1);

        // Link to next activity (EnterCourseActivity)
        Intent intent = new Intent(this, EnterCourseActivity.class);
        intent.putExtra("uuid", new UUIDManager(getApplicationContext()).getUserUUID());
        startActivity(intent);
        finish();
    }
}
