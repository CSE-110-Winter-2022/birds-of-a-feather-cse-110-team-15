package com.example.birdsofafeather;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.StudentWithCourses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StartStopSearchActivity extends AppCompatActivity {
    private Button StartButton;
    private Button StopButton;
    private boolean isSearchingOn;
    List<StudentWithCourses> students; // list of the students
    private StudentWithCourses me; // me, the user
    private RecyclerView studentsRecycleView;
    private RecyclerView.LayoutManager studentsLayoutManager;
    private StudentsViewAdapter studentsViewAdapter;
    private AppDatabase db;

    //list of pairs, each of which has a student and the number of common courses with the user
    private List<Pair<StudentWithCourses, Integer>> studentAndCountPairList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_stop_search);
        StopButton = (Button) findViewById(R.id.stop_button);
        StopButton.setVisibility(View.INVISIBLE);

        isSearchingOn = false; // on create, the search is off
        db = AppDatabase.singleton(this);
        me = db.studentWithCoursesDao().get(1); // get the student with id 1

        // Set up the RecycleView for the list of students
        studentAndCountPairList = new ArrayList<>(); // on creation, it's an empty list
        studentsRecycleView = findViewById(R.id.students_recycler_view);
        studentsLayoutManager = new LinearLayoutManager(this);
        studentsRecycleView.setLayoutManager(studentsLayoutManager);
        studentsViewAdapter = new StudentsViewAdapter(studentAndCountPairList);
        studentsRecycleView.setAdapter(studentsViewAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume(){
        super.onResume();

        // if the search is off, do nothing
        if (!isSearchingOn){
            return;
        }

        // else, update the list when the activity is resumed
        students = db.studentWithCoursesDao().getAll();
        studentAndCountPairList = createStudentCountPairList(me, students);
        studentsViewAdapter.updateStudentAndCoursesCountPairs(studentAndCountPairList);

    }

    public void onStartClick(View view) {
        //start bluetooth

        //hide start
        StartButton = (Button)findViewById(R.id.start_button);
        StartButton.setVisibility(View.INVISIBLE);

        //show stop
        StopButton = (Button) findViewById(R.id.stop_button);
        StopButton.setVisibility(View.VISIBLE);

        isSearchingOn = true;

        students = db.studentWithCoursesDao().getAll();
        studentAndCountPairList = createStudentCountPairList(me, students);
        studentsViewAdapter.updateStudentAndCoursesCountPairs(studentAndCountPairList);
    }

    public void onStopClick(View view) {
        //stop bluetooth

        //hide stop
        StopButton = (Button)findViewById(R.id.stop_button);
        StopButton.setVisibility(View.INVISIBLE);

        //show start
        StartButton = (Button) findViewById(R.id.start_button);
        StartButton.setVisibility(View.VISIBLE);

        isSearchingOn = false;
    }


    // create a list of pairs of student and the number of common courses with me
    // from a StudentWithCourses object (me) and the list of StudentWithCourses
    public List<Pair<StudentWithCourses, Integer>> createStudentCountPairList
            (StudentWithCourses me, @NonNull List<StudentWithCourses> students) {
        List<Pair<StudentWithCourses, Integer>> studentAndCountPairs = new ArrayList<>();
        int count;
        Pair<StudentWithCourses, Integer> newPair;

        students.remove(0); // remove me from the student

        for (StudentWithCourses student : students) {
            count = me.getCommonCourses(student).size();

            // add a pair of this student and count if the student has at least one common course with me
            if (count > 0){
                newPair = new Pair<StudentWithCourses, Integer>(student, count);
                studentAndCountPairs.add(newPair);
            }
        }

        // sort the list by the number of common courses in descending order
        Collections.sort(studentAndCountPairs, new Comparator<Pair<StudentWithCourses, Integer>>() {
            @Override
            public int compare(Pair<StudentWithCourses, Integer> studentWithCoursesIntegerPair, Pair<StudentWithCourses, Integer> t1) {
                return t1.second - studentWithCoursesIntegerPair.second;
            }
        });

        return studentAndCountPairs;
    }

    public void onMockClicked(View view) {
        // TODO: link to the mock arrival of nearby messages
    }
}
