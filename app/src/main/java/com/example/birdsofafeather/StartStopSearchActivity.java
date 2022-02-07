package com.example.birdsofafeather;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StartStopSearchActivity extends AppCompatActivity {
    private Button StartStopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_stop_search);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void onStartClick(View view) {
        //start bluetooth

        //change start to stop
        StartStopButton = (Button)findViewById(R.id.start_stop_button);
        StartStopButton.setText("STOP");

        //change stop to start
        StartStopButton = (Button)findViewById(R.id.start_stop_button);
        StartStopButton.setText("START");

    }
}
