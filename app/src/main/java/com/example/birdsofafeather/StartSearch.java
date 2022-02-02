package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartSearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_search);
    }

    public void onStartClick(View view) {
        //start bluetooth

        Intent intent = new Intent(this, StopSearch.class);
        startActivity(intent);

        finish();
    }
}