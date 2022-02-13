package com.example.birdsofafeather;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

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
            // first line has to be name
            String name = "";
            // second line has to be headshot URL
            String headshotURL = "";
            // third line onwards are courses
            List<String> courses = new ArrayList<>();
            int i = 0;
            for (String line : data.split(System.lineSeparator())) {
                if (i == 0) {
                    name = line.split(",").toString();
                } else if (i == 1) {
                    headshotURL = line.split(",").toString();
                } else {
                    courses.add(line.replaceAll("\\,", " "));
                }
                i++;
            }

            // create objects
            AppDatabase db = AppDatabase.singleton(getApplicationContext());
            int student_id = db.studentWithCoursesDao().lastIdCreated() + 1;
            db.studentWithCoursesDao().insert(new Student(name, headshotURL));
            for (String course : courses) {
                db.coursesDao().insert(new Course(student_id, course));
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