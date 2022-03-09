package com.example.birdsofafeather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.nearby.messages.MessageListener;

public class MockScreenActivity extends AppCompatActivity {
    private static final String TAG = "MOCK SCREEN";

    // used to help facilitate binding/unbinding the NearbyBackgroundService
    // to the MockScreenActivity
    private final BoFServiceConnection serviceConnection = new BoFServiceConnection();
    private FakedMessageListener fakedMessageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_screen);

        // initiate faked listener to mock sending/receiving data
        String uuid = new UUIDManager(this).getUserUUID();
        fakedMessageListener = new FakedMessageListener(new NearbyMessagesFactory().build(uuid, this));
    }

    public void onEnterDataClicked(View view) {
        TextView inputDataTextView = findViewById(R.id.input_data_view);
        String inputString = inputDataTextView.getText().toString();
        // clear TextView to show processing
        inputDataTextView.setText("");

        // send/publish message to service to relay to messageListener
        fakedMessageListener.receive(inputString);
    }

    public void onGoBackClicked(View view) {
        finish();
    }
}