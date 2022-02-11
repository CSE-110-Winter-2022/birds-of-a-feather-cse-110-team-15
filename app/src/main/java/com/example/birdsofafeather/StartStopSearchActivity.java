package com.example.birdsofafeather;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        // Setting up the RecycleView for the list of students
        studentAndCountPairList = new ArrayList<>();
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

    public void onStartClick(View view) {
        //start bluetooth

        //hide start
        StartButton = (Button)findViewById(R.id.start_button);
        StartButton.setVisibility(View.INVISIBLE);

        //show stop
        StopButton = (Button) findViewById(R.id.stop_button);
        StopButton.setVisibility(View.VISIBLE);

        //
        db = AppDatabase.singleton(this);
        List<StudentWithCourses> students = db.studentWithCoursesDao().getAll();
        StudentWithCourses me = db.studentWithCoursesDao().get(1);
        studentAndCountPairList = createStudentCountPairList(me, students);
        studentsViewAdapter.setStudentAndCoursesCountPairs(studentAndCountPairList);
    }

    public void onStopClick(View view) {
        //stop bluetooth

        //hide stop
        StopButton = (Button)findViewById(R.id.start_button);
        StopButton.setVisibility(View.INVISIBLE);

        //show start
        StartButton = (Button) findViewById(R.id.stop_button);
        StartButton.setVisibility(View.VISIBLE);

    }

    public List<Pair<StudentWithCourses, Integer>> createStudentCountPairList
            (StudentWithCourses me, List<StudentWithCourses> students) {
        List<Pair<StudentWithCourses, Integer>> studentAndCountPairs = new ArrayList<>();
        int count;
        students.remove(0); // remove me from the student
        Pair<StudentWithCourses, Integer> newPair;

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
}
