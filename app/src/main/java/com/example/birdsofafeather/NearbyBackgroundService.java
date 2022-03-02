package com.example.birdsofafeather;

import android.app.Activity;
import android.app.Presentation;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.CourseDao;
import com.example.birdsofafeather.models.db.Student;
import com.example.birdsofafeather.models.db.StudentWithCourses;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class NearbyBackgroundService extends Service {
    private String TAG = "NearbyBackground";
    private final NearbyBinder binder = new NearbyBinder();
    private String uuid;
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
            // first line has to be the uuid
            String senderUUID = "";
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
                    senderUUID = line.split(",")[0];
                } else if (i == 1) {
                    name = line.split(",")[0];
                } else if (i == 2) {
                    headshotURL = line.split(",")[0];
                } else {
                    List<String> courseOrWave = Arrays.asList(line.split(","));
                    if (courseOrWave.get(1).equals("wave")) {
                        String receiverUUID = courseOrWave.get(0);

                        // Method that takes the ID of the person the wave is for, and returns true
                        // if the wave was at the current user
                        if (uuid.equals(receiverUUID))
                            wavedAtCurrentUser = true;
                    } else {
                        courses.add(String.join(" ", courseOrWave.get(2), courseOrWave.get(3), courseOrWave.get(1), courseOrWave.get(0)));
                    }
                }
                i++;
            }
            // get sessionId from sharedPreferences
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            int sessionId = preferences.getInt("sessionId", 0);
            if (sessionId == 0) {
                // log and return
                Log.d(TAG, "No active session!");
                return;
            }

            // create objects
            AppDatabase db = AppDatabase.singleton(getApplicationContext());

            // If the student already existed, get old values and update the student in the database
            if ( db.studentWithCoursesDao().getStudentWithSession(senderUUID, sessionId) != null) {
                StudentWithCourses oldStudent = db.studentWithCoursesDao().getStudentWithSession(senderUUID, sessionId);
                boolean oldWavedTo = (oldStudent.getWavedToUser() || wavedAtCurrentUser);
                boolean oldWavedFrom = oldStudent.getWavedFromUser();
                boolean oldFavorite = oldStudent.isFavorite();

                db.studentWithCoursesDao().updateFavorite(senderUUID, oldFavorite);
                db.studentWithCoursesDao().updateWaveFrom(senderUUID, oldWavedFrom);
                db.studentWithCoursesDao().updateWaveTo(senderUUID, oldWavedTo);
            } else {
                boolean setFavorite = false;
                if ( db.studentWithCoursesDao().get(senderUUID) != null) {
                    StudentWithCourses oldStudent = db.studentWithCoursesDao().get(senderUUID);
                    setFavorite =  oldStudent.isFavorite();
                }

                db.studentWithCoursesDao().insert(new Student(senderUUID, name, headshotURL, sessionId, wavedAtCurrentUser, setFavorite));

            }

            for (String course : courses) {
                if (db.coursesDao().getCourseWithStudent(course, senderUUID) == null) {
                    db.coursesDao().insert(new Course(senderUUID, course));
                }
            }
        }
    };
    // our mock of Nearby Messages API
    private FakedMessageListener messageListener = new FakedMessageListener(realListener);

    public NearbyBackgroundService() {}

    @Override
    public IBinder onBind(Intent intent) {
        uuid = intent.getStringExtra("uuid");
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