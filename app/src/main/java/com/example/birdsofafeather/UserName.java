package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class UserName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
        loadProfile();
    }

    @Override
    protected void onDestroy() {
        saveProfile();
        super.onDestroy();
    }

    public void loadProfile() {

    }

    public void saveProfile() {

    }

    public void onConfirmNameClick(View view) {
        finish();
    }
}