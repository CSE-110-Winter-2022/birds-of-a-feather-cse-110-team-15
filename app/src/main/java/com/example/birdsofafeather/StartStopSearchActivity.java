package com.example.birdsofafeather;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StartStopSearchActivity extends AppCompatActivity {
    private Button StartButton;
    private Button StopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_stop_search);
        StopButton = (Button) findViewById(R.id.stop_button);
        StopButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void onStartClick(View view) {
        //start bluetooth

        //hide start
        StartButton = (Button)findViewById(R.id.start_button);
        StartButton.setVisibility(View.INVISIBLE);

        //show stop
        StopButton = (Button) findViewById(R.id.stop_button);
        StopButton.setVisibility(View.VISIBLE);

    }

    public void onStopClick(View view) {
        //stop bluetooth

        //hide stop
        StopButton = (Button)findViewById(R.id.start_button);
        StopButton.setVisibility(View.INVISIBLE);

        //show start
        StartButton = (Button) findViewById(R.id.stop_button);
        StartButton.setVisibility(View.VISIBLE);

    }
}
