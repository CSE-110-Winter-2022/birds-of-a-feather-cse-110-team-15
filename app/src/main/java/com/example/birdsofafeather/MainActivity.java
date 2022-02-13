package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    GoogleLoginHandler loginHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginHandler = new GoogleLoginHandler(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!loginHandler.isUserSignedIn()) {
            loginHandler.signIn();
        }
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