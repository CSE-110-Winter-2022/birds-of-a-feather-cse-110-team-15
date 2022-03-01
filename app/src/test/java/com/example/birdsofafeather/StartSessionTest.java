package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.Spinner;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.Session;
import com.example.birdsofafeather.models.db.SessionWithStudents;
import com.example.birdsofafeather.models.db.Student;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class StartSessionTest {
    private AppDatabase db;
    private View startSessionPopupView;
    private Spinner startSessionSpinner;


    @Before
    public void init() {
       AppDatabase.useTestSingleton(ApplicationProvider.getApplicationContext());
       db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
       // this student is user of the app
       db.studentWithCoursesDao().insert(new Student("s1ID","John", "url.com"));
       db.coursesDao().insert(new Course("s1ID", "CSE 21 FA 2020"));
       db.coursesDao().insert(new Course("s1ID", "CSE 30 WI 2021"));
       db.coursesDao().insert(new Course("s1ID", "CSE 100 SP 2021"));
       db.coursesDao().insert(new Course("s1ID", "CSE 105 FA 2021"));
    }

    @After
    public void teardown() {
        db.close();
    }

    /**
     * Makes the start session popup for testing purposes
     * @param activity current activity to create the popup at
     */
    public void makeStartSessionPopup(Activity activity) {
        List<String> sessions = new ArrayList<>();
        sessions.add("New Session");
        for (SessionWithStudents session : db.sessionWithStudentsDao().getAll()) {
            sessions.add(session.getName());
        }
        // set content in spinner
        startSessionSpinner = startSessionPopupView.findViewById(R.id.session_spinner);
        ArrayAdapter<String> startSessionAdapter = new ArrayAdapter<>(activity.getApplicationContext(), android.R.layout.simple_spinner_item, sessions);
        startSessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startSessionSpinner.setAdapter(startSessionAdapter);
        startSessionSpinner.setSelection(0, true);
        // create popup
        int params = ViewGroup.LayoutParams.WRAP_CONTENT;
        PopupWindow popupWindow = new PopupWindow(startSessionPopupView, params, params, true);
        popupWindow.showAtLocation(activity.findViewById(android.R.id.content).getRootView(), Gravity.CENTER, 0, 0);
    }

    @Test
    /**
     * Test user opening start session popup for the first time
     * - should see only one option to create 'New Session'
     */
    public void testFirstTimeStartingSessionPopUp() {
        try(ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(StartStopSearchActivity.class)) {
            scenario.onActivity(activity -> {
                // get Popup View
                startSessionPopupView = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.start_session_popup, null);
                // make popup
                makeStartSessionPopup(activity);
                // check spinner's only option is to create new session
                assertEquals("Selected option", "New Session", startSessionSpinner.getSelectedItem().toString());
                assertEquals("Size of spinner", 1, startSessionSpinner.getCount());
            });
        }
    }

    @Test
    /**
     * Test user opening start session popup after already creating 2 sessions
     * - should see 3 choices in spinner: 'New Session', 'CSE 100', and 'COGS 109'
     */
    public void testResumingSessionPopup() {
        // insert sessions into database for test
        db.sessionWithStudentsDao().insert(new Session("CSE 100"));
        db.sessionWithStudentsDao().insert(new Session("COGS 109"));

        try(ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(StartStopSearchActivity.class)) {
            scenario.onActivity(activity -> {
                // get Popup View
                startSessionPopupView = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.start_session_popup, null);
                // make popup
                makeStartSessionPopup(activity);
                // check spinner's only option is to create new session
                assertEquals("Selected option", "New Session", startSessionSpinner.getSelectedItem().toString());
                startSessionSpinner.setSelection(1);
                assertEquals("Selected option", "CSE 100", startSessionSpinner.getSelectedItem().toString());
                startSessionSpinner.setSelection(2);
                assertEquals("Selected option", "COGS 109", startSessionSpinner.getSelectedItem().toString());
                assertEquals("Size of spinner", 3, startSessionSpinner.getCount());
            });
        }
    }

    @Test
    /**
     * Test seeing list after popup for selecting "New Session"
     */
    public void testNewSessionList() {
        // simulate creating new session and updating Shared Preferences
        db.sessionWithStudentsDao().insert(new Session("session1"));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext());
        preferences.edit().putInt("sessionId", 1);
        try(ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(StartStopSearchActivity.class)){
            scenario.onActivity(activity -> {
                RecyclerView studentList = activity.findViewById(R.id.students_recycler_view);
                final int studentCount = studentList.getChildCount();

                // There are 0 sessions in database, so no students should be in view
                assertEquals("Student count", 0, studentCount);
            });
        }
    }
}
