package com.example.birdsofafeather;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.util.StringUtil;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.Student;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.List;

public class NearbyBackgroundService extends Service {
    private String TAG = "NearbyBackground";
    private final NearbyBinder binder = new NearbyBinder();
    private final MessageListener realListener = new MessageListener() {
    private final UUIDManager uuidManager = new UUIDManager(getApplicationContext());

        @Override
        public void onFound(@NonNull Message message) {
            String data = new String(message.getContent());
            Log.d(TAG, "Found message: " + data);
            // create student and course objects
            processData(data);
        }

        @Override
        public void onLost(@NonNull Message message) {
            Log.d(TAG, "Lost sight of message: " + new String(message.getContent()));
        }

        public void processData(String data) {
            // first line has to be the uuid
            String uuid = "";
            // second line has to be name
            String name = "";
            // third line has to be headshot URL
            String headshotURL = "";
            // fourth line onwards are courses or wave messages
            List<String> courses = new ArrayList<>();
            boolean wavedAtCurrentUser = false;

            int i = 0;
            for (String line : data.split(System.lineSeparator())) {
                if (i == 0) {
                    uuid = line.split(",")[0];
                } else if (i == 1) {
                    name = line.split(",")[0];
                } else if (i == 2) {
                    headshotURL = line.split(",")[0];
                } else {
                    String[] courseOrWave = line.split(",");
                    if (courseOrWave[1].equals("wave")) {
                        String receiverUUID = courseOrWave[0];

                        // Method that takes the ID of the person the wave is for, and returns true
                        // if the wave was at the current user
                        if (uuidManager.match(uuid))
                            wavedAtCurrentUser = true;

                    } else {
                        courses.add(String.join(" ", courseOrWave));
                    }
                }
                i++;
            }

            // create objects
            AppDatabase db = AppDatabase.singleton(getApplicationContext());
            db.studentWithCoursesDao().insert(new Student(uuid, name, headshotURL, wavedAtCurrentUser));
            for (String course : courses) {
                db.coursesDao().insert(new Course(uuid, course));
            }
        }
    };
    // our mock of Nearby Messages API
    private FakedMessageListener messageListener = new FakedMessageListener(realListener);

    public NearbyBackgroundService() { }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    // allow for rebind
    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    // start receiving messages from NearbyMessages
    public void subscribe(Activity activity) {
        Log.d(TAG, "Subscribing...");
        Nearby.getMessagesClient(activity).subscribe(messageListener);
    }

    // stop receiving messages from NearbyMessages
    public void unsubscribe(Activity activity) {
        Log.d(TAG, "Unsubscribing...");
        Nearby.getMessagesClient(activity).unsubscribe(messageListener);
    }

    // mock publishing a message to server
    public void publish(String message) {
        Log.d(TAG, "Publishing...");
        messageListener.receive(message);
    }

    class NearbyBinder extends Binder implements IBinder {
        public NearbyBackgroundService getService() {
            return NearbyBackgroundService.this;
        }
    }
}