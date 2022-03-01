package com.example.birdsofafeather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.models.db.AppDatabase;

import com.example.birdsofafeather.models.db.Session;
import com.example.birdsofafeather.models.db.SessionWithStudents;
import com.example.birdsofafeather.models.db.StudentWithCourses;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartStopSearchActivity extends AppCompatActivity {
    private Button StartButton;
    private Button StopButton;
    private StudentWithCourses me; // me, the user
    private StudentsViewAdapter studentsViewAdapter;
    private AppDatabase db;
    private SharedPreferences preferences;
    private Handler handler = new Handler();
    private Runnable runnable;
    private final int updateListDelay = 5000; // update the list every 5 seconds
    private View startSessionPopupView;
    private Spinner startSessionSpinner;
    private int sessionId;
    private View savePopupView;
    private boolean isNewSession;
    private TextView sessionTitle;

    private Map<String, Integer> sessionIdMap;

    //list of pairs, each of which has a student and the number of common courses with the user
    private List<Pair<StudentWithCourses, Integer>> studentAndCountPairList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_stop_search);
        setTitle(getString(R.string.app_name));
        StopButton = findViewById(R.id.stop_button);
        StopButton.setVisibility(View.INVISIBLE);

        StartButton = findViewById(R.id.start_button);
        StartButton.setOnClickListener((View view) -> onStartClick(view));

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        db = AppDatabase.singleton(this);
        me = db.studentWithCoursesDao().get(1); // get the student with id 1

        // Set up the RecycleView for the list of students
        studentAndCountPairList = new ArrayList<>(); // on creation, it's an empty list
        RecyclerView studentsRecycleView = findViewById(R.id.students_recycler_view);
        RecyclerView.LayoutManager studentsLayoutManager = new LinearLayoutManager(this);
        studentsRecycleView.setLayoutManager(studentsLayoutManager);

        // Pass in student list and function to update favorite status to the adapter
        studentsViewAdapter = new StudentsViewAdapter(studentAndCountPairList, (student) ->
                db.studentWithCoursesDao().updateStudent(student)
        );
        studentsRecycleView.setAdapter(studentsViewAdapter);

        // get startSessionPopupView
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        startSessionPopupView = layoutInflater.inflate(R.layout.start_session_popup, null);
        // set savePopupView
        savePopupView = layoutInflater.inflate(R.layout.save_popup_window, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume();

        // if the search is off, do nothing
        if (StopButton.getVisibility() == View.INVISIBLE){
            return;
        }

        // else, update the student list when the activity is resumed
        updateRecyclerView();

        // and start updating the list for every 5 second again
        handler.postDelayed(runnable, updateListDelay);
    }

    @Override
    protected void onPause(){
        super.onPause();

        // stop updating the recycler view when another activity comes to the front
        handler.removeCallbacks(runnable);
    }

    public void onStartClick(View view) {
        //start bluetooth

        // add all the sessions found in database to sessions list
        List<String> sessions = new ArrayList<>();
        sessions.add("New Session");
        sessionIdMap = new HashMap<>();
        List<SessionWithStudents> storedSessions = db.sessionWithStudentsDao().getAll();
        for (SessionWithStudents session : storedSessions) {
            sessions.add(session.getName());
            sessionIdMap.put(session.getName(), session.getSessionId());
        }

        // set content in spinner
        startSessionSpinner = startSessionPopupView.findViewById(R.id.session_spinner);
        ArrayAdapter<String> startSessionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sessions);
        startSessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startSessionSpinner.setAdapter(startSessionAdapter);
        startSessionSpinner.setSelection(0, true);

        // create popup
        PopupWindow popupWindow = new PopupWindow(startSessionPopupView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(findViewById(android.R.id.content).getRootView(), Gravity.CENTER, 0, 0);

        // set onclick for button to start session
        Button startSessionBtn = startSessionPopupView.findViewById(R.id.start_session_button);
        startSessionBtn.setOnClickListener((View v) -> {
            onStartSessionClicked(v);
            popupWindow.dismiss(); // close popup
        });
    }

    public void onStartSessionClicked(View view) {
        String session = (String) startSessionSpinner.getSelectedItem();

        String curSession;
        // check if new session or existing session
        if (session.equals("New Session")) {
            // session title will be current date
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
            curSession = formatter.format(new Date());
            // insert a new session into database
            sessionId = (int) db.sessionWithStudentsDao().insert(new Session(curSession));
            isNewSession = true;
        } else {
            // set id based on selected session name
            curSession = (String) startSessionSpinner.getSelectedItem();
            sessionId = sessionIdMap.get(curSession);
            isNewSession = false;
        }

        // put sessionId into shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit()
                .putInt("sessionId", sessionId)
                .apply();

        // set title of session
        sessionTitle = findViewById(R.id.cur_session);
        sessionTitle.setText(curSession);

        // hide start button
        StartButton = findViewById(R.id.start_button);
        StartButton.setVisibility(View.INVISIBLE);

        // show stop button
        StopButton = findViewById(R.id.stop_button);
        StopButton.setVisibility(View.VISIBLE);

        // update the recycler view based on the current student list
        updateRecyclerView();

        handler.postDelayed (runnable = () -> {
            updateRecyclerView();
            handler.postDelayed(runnable, updateListDelay);
        }, updateListDelay);
    }

    public void onStopClick(View view) {
        //stop bluetooth

        //hide stop
        StopButton = findViewById(R.id.stop_button);
        StopButton.setVisibility(View.INVISIBLE);

        //show start
        StartButton = findViewById(R.id.start_button);
        StartButton.setVisibility(View.VISIBLE);

        // stop updating the recycler view
        handler.removeCallbacks(runnable);

        // get the session id that is passed from onStartSessionClicked
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // if this is a new session, then show popup to name it
        if (isNewSession){
            createSavePopup(view);
        }

        // clear preference
        preferences.edit().clear().apply();
    }

    public void onMockClicked(View view) {
        Intent intent = new Intent(this, MockScreenActivity.class);
        startActivity(intent);
    }

    public void onLabelClicked(View view) {
        // if session is currently stopped, then return
        if (StopButton.getVisibility() == View.INVISIBLE)
            return;

        // else show popup to name session
        createSavePopup(view);
    }

    // create a list of pairs of student and the number of common courses with me
    // from a StudentWithCourses object (me) and the list of StudentWithCourses
    public List<Pair<StudentWithCourses, Integer>> createStudentAndCountPairList
    (StudentWithCourses me, @NonNull List<StudentWithCourses> otherStudents) {
        List<Pair<StudentWithCourses, Integer>> studentAndCountPairs = new ArrayList<>();
        int count; // count of common courses

        // create a list of pair of student and the number of common courses
        for (StudentWithCourses student : otherStudents) {
            count = me.getCommonCourses(student).size();

            // add a pair of this student and count if the student has at least one common course with me
            if (count > 0){
                studentAndCountPairs.add(new Pair<>(student, count));
            }
        }

        // sort the list by the number of common courses in descending order
        Collections.sort(studentAndCountPairs, (s1, s2) -> s2.second - s1.second);

        return studentAndCountPairs;
    }

    // update the recycler view based on the current session in the database.
    public void updateRecyclerView() {
        sessionId = preferences.getInt("sessionId", 0);
        // if no session id in shared preferences, don't update recycler view
        if (sessionId == 0) {
            Log.d("StartStopSearchActivity", "Not currently in a session!");
            return;
        }
        // get students of current session
        List<StudentWithCourses> otherStudents = db.sessionWithStudentsDao().get(sessionId).getStudents();

        studentAndCountPairList = createStudentAndCountPairList(me, otherStudents);
        // update recycler based on student list obtained from sessions
        studentsViewAdapter.updateStudentAndCoursesCountPairs(studentAndCountPairList);
    }

    // create a popup for saving a new session and also renaming session
    public void createSavePopup(View view){
        sessionId = preferences.getInt("sessionId",0); // session id
        // create up a popup window
        PopupWindow savePopupWindow = new PopupWindow(savePopupView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        savePopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // set date and time session has started as hint in the session name
        String currentTime = db.sessionWithStudentsDao().get(sessionId).getName();
        TextView sessionNameView = savePopupView.findViewById(R.id.session_name_view);
        sessionNameView.setHint(currentTime);

        // create a list of current courses
        List<String> allCourses = db.studentWithCoursesDao().get(1).getCourses(); // list of the courses of the user
        List<String> currentCourses = new ArrayList<>();
        String defaultMessage = "(Current Course)";
        currentCourses.add(defaultMessage);

        // add course to currentCourse if it is from winter 2022
        for (String course: allCourses){
            // parse the course string. an example of current course string is "CSE 110 WI 2022"
            String[] tokens = course.split(" ");

            // check if the course is in winter 2022
            if (tokens[2].equals("WI") && tokens[3].equals("2022")) {
                currentCourses.add(course);
            }
        }

        // set up the current course dropdown
        Spinner coursesSpinner = (Spinner) savePopupView.findViewById(R.id.current_courses_spinner);
        ArrayAdapter<String> coursesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currentCourses);
        coursesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coursesSpinner.setAdapter(coursesAdapter);

        // set up the save button and its onClick event
        Button saveSessionButton = (Button) savePopupView.findViewById(R.id.save_session_button);
        saveSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sessionName;

                // get the selected item in the current course dropdown
                String selectedCourse = coursesSpinner.getSelectedItem().toString();

                // if the user actually selected a course, then use that name
                if (selectedCourse != defaultMessage){
                    sessionName = selectedCourse;
                }
                // else check the user input
                else {
                    // get the user input for session name
                    String userInput = sessionNameView.getText().toString();
                    // set session name to user input
                    // if no name is provided, save the session with the date and time
                    sessionName = userInput.isEmpty() ? currentTime : userInput;
                }

                // update the name of the current session
                Session currentSession = db.sessionWithStudentsDao().get(sessionId).getSession();
                currentSession.setName(sessionName);
                db.sessionWithStudentsDao().updateSession(currentSession);

                sessionNameView.setText("");  // clear input field
                sessionTitle.setText(sessionName); // update session title view

                savePopupWindow.dismiss();
            }
        });
    }
}