package com.example.birdsofafeather;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.Student;
import com.example.birdsofafeather.models.db.StudentWithCourses;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NearbyMessagesFactory {
    private final String TAG = "MESSAGE LISTENER";

    public MessageListener build(String uuid, Context context) {
        return new MessageListener() {
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
                        // make sure not to add yourself to the list
                        if (uuid.equals(senderUUID)) return;
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
                            // if wave isn't directed at you, shouldn't process it
                            else return;
                        } else {
                            courses.add(String.join(" ", courseOrWave));
                        }
                    }
                    i++;
                }
                // get sessionId from sharedPreferences
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                int sessionId = preferences.getInt("sessionId", 0);
                if (sessionId == 0) {
                    // log and return
                    Log.d(TAG, "No active session!");
                    return;
                }

                // create objects
                AppDatabase db = AppDatabase.singleton(context);

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
    }

    // converts a student object into a string that can be sent via Nearby Messages
    public Message buildMessage(StudentWithCourses student, String sendWaveUUID) {
        // should not ever be null, but just send an empty message
        if (student == null) {
            Log.d("NearbyMessagesFactory", "student object is null");
            return new Message("".getBytes(StandardCharsets.UTF_8));
        }
        // order: UUID, Name, Headshot URL, Classes/Waves
        // add UUID, Name, Headshot URL
        String data = student.getUUID() + ",,,\n" + student.getName() + ",,,\n" + student.getHeadshotURL() + ",,,\n";
        // add courses
        for (String course : student.getCourses()) {
            String[] split = course.split(" ");
            data += split[0] + "," + split[1] + "," + split[2] + "," + split[3] + "," + split[4] + "\n";
        }
        if (sendWaveUUID != null) {
            data += sendWaveUUID + ",wave,,\n";
        }
        return new Message(data.getBytes(StandardCharsets.UTF_8));
    }
}
