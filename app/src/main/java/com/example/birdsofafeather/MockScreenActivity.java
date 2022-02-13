package com.example.birdsofafeather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MockScreenActivity extends AppCompatActivity {
    private static final String TAG = "MOCK SCREEN";
    private boolean isBound;
    private NearbyBackgroundService nearbyService;

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            NearbyBackgroundService.NearbyBinder nearbyBinder = (NearbyBackgroundService.NearbyBinder)iBinder;
            nearbyService = nearbyBinder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_screen);

        Intent intent = new Intent(this, NearbyBackgroundService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
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
        if (isBound)  {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    public void onEnterDataClicked(View view) {
        TextView inputDataTextView = findViewById(R.id.input_data_view);
        String inputString = inputDataTextView.getText().toString();
        // clear TextView to show processing
        inputDataTextView.setText("");

        // send/publish message to service to relay to messageListener
        nearbyService.publish(inputString);
    }

    public void onGoBackClicked(View view) {
        finish();
    }
}