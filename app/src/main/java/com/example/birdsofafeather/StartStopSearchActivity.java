package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.StudentWithCourses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StartStopSearchActivity extends AppCompatActivity {
    private Button StartButton;
    private Button StopButton;
    private StudentWithCourses me; // me, the user
    private RecyclerView studentsRecycleView;
    private RecyclerView.LayoutManager studentsLayoutManager;
    private StudentsViewAdapter studentsViewAdapter;
    private AppDatabase db;
    private Handler handler = new Handler();
    private Runnable runnable;
    private CheckBox fav;
    private int updateListDelay = 5000; // update the list every 5 seconds

    //list of pairs, each of which has a student and the number of common courses with the user
    private List<Pair<StudentWithCourses, Integer>> studentAndCountPairList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_stop_search);
        StopButton = (Button) findViewById(R.id.stop_button);
        StopButton.setVisibility(View.INVISIBLE);

        db = AppDatabase.singleton(this);
        me = db.studentWithCoursesDao().get(1); // get the student with id 1

        // Set up the RecycleView for the list of students
        studentAndCountPairList = new ArrayList<>(); // on creation, it's an empty list
        studentsRecycleView = findViewById(R.id.students_recycler_view);
        studentsLayoutManager = new LinearLayoutManager(this);
        studentsRecycleView.setLayoutManager(studentsLayoutManager);
        studentsViewAdapter = new StudentsViewAdapter(studentAndCountPairList);
        studentsRecycleView.setAdapter(studentsViewAdapter);

        // update the recycler view based on the current student list
        updateRecyclerViewIfNonEmpty();

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
        updateRecyclerViewIfNonEmpty();

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

        //hide start
        StartButton = (Button)findViewById(R.id.start_button);
        StartButton.setVisibility(View.INVISIBLE);

        //show stop
        StopButton = (Button) findViewById(R.id.stop_button);
        StopButton.setVisibility(View.VISIBLE);

        // update the recycler view based on the current student list
        updateRecyclerViewIfNonEmpty();

        // update students recycler view every 5 seconds based on the database change
        handler.postDelayed (runnable = new Runnable() {
            @Override
            public void run() {
                updateRecyclerViewIfNonEmpty();
                handler.postDelayed(runnable, updateListDelay);
            }
        }, updateListDelay);
    }

    public void onStopClick(View view) {
        //stop bluetooth

        //hide stop
        StopButton = (Button)findViewById(R.id.stop_button);
        StopButton.setVisibility(View.INVISIBLE);

        //show start
        StartButton = (Button) findViewById(R.id.start_button);
        StartButton.setVisibility(View.VISIBLE);

        // stop updating the recycler view
        handler.removeCallbacks(runnable);
    }

    public void onMockClicked(View view) {
        Intent intent = new Intent(this, MockScreenActivity.class);
        startActivity(intent);
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
                studentAndCountPairs.add(new Pair<StudentWithCourses, Integer>(student, count));
            }
        }

        // sort the list by the number of common courses in descending order
        Collections.sort(studentAndCountPairs, (s1, s2) -> { return s2.second - s1.second; });

        return studentAndCountPairs;
    }

    // update the recycler view based on the current data in the database.
    // but if the student list is empty for some reason (i.e. when there is bluetooth issue),
    // don't make any change on the recycler view, so the previous information is retained
    public void updateRecyclerViewIfNonEmpty() {
        List<StudentWithCourses> otherStudents = db.studentWithCoursesDao().getAll();
        otherStudents.remove(0); // remove myself
        studentAndCountPairList = createStudentAndCountPairList(me, otherStudents);
        if (!otherStudents.isEmpty())
            studentsViewAdapter.updateStudentAndCoursesCountPairs(studentAndCountPairList);
    }
}
