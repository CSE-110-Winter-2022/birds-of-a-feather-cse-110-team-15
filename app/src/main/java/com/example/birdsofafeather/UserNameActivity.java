package com.example.birdsofafeather;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.StudentWithCourses;

public class UserNameActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);

        TextView ConfirmName = (TextView) findViewById(R.id.name_view);
        ConfirmName.setVisibility(View.INVISIBLE);



    }

    @Override
    protected void onDestroy() {
        //  saveProfile();
        super.onDestroy();
    }


    public void onConfirmNameClick(View view) {
        // load name;
        int test_id = 3;
        AppDatabase db = AppDatabase.singleton(getApplicationContext());

        //TODO: Replace test_id with an intent from classmate list activity during linking task
        StudentWithCourses student = db.studentWithCoursesDao().get(test_id);

        // Set user name
        TextView nameView = findViewById(R.id.name_view);
        nameView.setText(student.getName());

        // Set profile name
        TextView userNameView = findViewById(R.id.name_view);
        userNameView.setText(student.getName());

        //show name
        TextView showConfirmName = (TextView) findViewById(R.id.name_view);
        showConfirmName.setVisibility(View.VISIBLE);

    }

    public void onContinueNameClick(View view){
        //get the intent
//        Intent intent = new Intent(this,InputHeadshotActivity.class);
//
//        TextView InputUserNameView = findViewById(R.id.input_name_textview);
//        String getInputUserNameView = InputUserNameView.getText().toString();
//
//        //set extra
//        intent.putExtra("student_name", getInputUserNameView);

        finish();
    }
}



