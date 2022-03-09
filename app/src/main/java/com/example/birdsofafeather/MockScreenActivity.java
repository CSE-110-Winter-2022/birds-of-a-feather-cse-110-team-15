package com.example.birdsofafeather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MockScreenActivity extends AppCompatActivity {
    private static final String TAG = "MOCK SCREEN";

    // used to help facilitate binding/unbinding the NearbyBackgroundService
    // to the MockScreenActivity
    private final BoFServiceConnection serviceConnection = new BoFServiceConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_screen);

        // bindService will initiate the bound service NearbyBackgroundService
        Intent intent = new Intent(this, NearbyBackgroundService.class);
        intent.putExtra("uuid", new UUIDManager(getApplicationContext()).getUserUUID());
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        serviceConnection.setBound(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    // make sure to stop service when ending activity
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceConnection.isBound())  {
            unbindService(serviceConnection);
            serviceConnection.setBound(false);
        }
    }

    public void onEnterDataClicked(View view) {
        TextView inputDataTextView = findViewById(R.id.input_data_view);
        String inputString = inputDataTextView.getText().toString();
        // clear TextView to show processing
        inputDataTextView.setText("");

        // send/publish message to service to relay to messageListener
        NearbyBackgroundService nearbyService = serviceConnection.getNearbyService();
        nearbyService.publish(inputString);
    }

    public void onGoBackClicked(View view) {
        finish();
    }
}