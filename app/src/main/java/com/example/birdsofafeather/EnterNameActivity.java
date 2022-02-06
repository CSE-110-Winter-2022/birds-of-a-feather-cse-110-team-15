package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EnterNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
    }

    public void displayName() {
        TextView studentNameView = findViewById(R.id.student_name_view);
        TextView studentNameInputView = findViewById(R.id.student_name_input_view);
        // set display name
        studentNameView.setText(studentNameInputView.getText().toString());
    }

    public void onConfirmNameClick(View view) {
        displayName();
    }

    public void onContinueClick(View view) {
        // TODO: make intent to go to 'upload headshot'
        // TODO: also pass on name for next activity to store into database
//        TextView studentNameView = findViewById(R.id.student_name_view);
//        String name = studentNameView.getText().toString();
//        Intent intent = new Intent(this, EnterHeadshotActivity.class);
//        intent.putExtra("student_name", name);
//        startActivity(intent);
        /**
         * To retrieve extras - in onCreate:
         * Bundle extras = getIntent().getExtras();
         * this.name = extras.getString("student_name");
         */
        finish();
    }
}