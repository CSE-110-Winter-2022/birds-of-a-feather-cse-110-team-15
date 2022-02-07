package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.birdsofafeather.models.db.AppDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onLoginClick(View view) {
        Intent intent = new Intent(this, StartStopSearchActivity.class);
        startActivity(intent);
    }

    public void onCreateProfile(View view) {
        Intent intent = new Intent(this, UserNameActivity.class);
        startActivity(intent);
    }
}