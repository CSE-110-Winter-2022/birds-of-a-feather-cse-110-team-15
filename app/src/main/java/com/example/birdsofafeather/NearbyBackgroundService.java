package com.example.birdsofafeather;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

public class NearbyBackgroundService extends Service {
    private String TAG = "NearbyBackground";
    private final NearbyBinder binder = new NearbyBinder();
    private String uuid;
    private MessageListener messageListener;

    public NearbyBackgroundService() {}

    @Override
    public IBinder onBind(Intent intent) {
        uuid = intent.getStringExtra("uuid");
        messageListener = new NearbyMessagesFactory().build(uuid, this);
        Nearby.getMessagesClient(this).subscribe(messageListener);
        return binder;
    }

    // allow for rebind
    @Override
    public boolean onUnbind(Intent intent) {
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
        return true;
    }

    // start receiving messages from NearbyMessages
    public void subscribe(Activity activity) {
        Log.d(TAG, "Subscribing...");
        Nearby.getMessagesClient(this).subscribe(messageListener);
    }

    // stop receiving messages from NearbyMessages
    public void unsubscribe(Activity activity) {
        Log.d(TAG, "Unsubscribing...");
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
    }

    // mock publishing a message to server
    public void publish(Message message) {
        Log.d(TAG, "Publishing...");
        Nearby.getMessagesClient(this).publish(message);
    }

    public void unpublish(Message message) {
        Log.d(TAG, "Unpublishing...");
        Nearby.getMessagesClient(this).unpublish(message);
    }

    class NearbyBinder extends Binder implements IBinder {
        public NearbyBackgroundService getService() {
            return NearbyBackgroundService.this;
        }
    }
}