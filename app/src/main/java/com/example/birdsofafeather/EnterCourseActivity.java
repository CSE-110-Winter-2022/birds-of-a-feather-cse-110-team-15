package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.birdsofafeather.models.IStudent;
import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class EnterCourseActivity extends AppCompatActivity {
    Spinner quarterSpinner, yearSpinner;
    List<Course> enteredCourses;
    AppDatabase db;
    int studentId = 0; // TODO: get the user's id. for now set it to 0

    private RecyclerView coursesRecyclerView;
    private RecyclerView.LayoutManager coursesLayoutManager;
    private CoursesViewAdapter coursesViewAdapter;

    String[] quarters = {"", "FA", "WI", "SP", "SS1", "SS2", "SSS"};
    String[] years = {"", "2022", "2021", "2020","2019","2018","2017","2016","2015","2014","2013"};

    // firstYear: Year UCSD was founded
    // maxYear: In a full release, could be replaced with call for current year.
    List<String> yearsList = makeYearsList(1960, 2022);

    // Take beginning and end years we want to support, create list of all years in between them
    private List<String> makeYearsList(int firstYear, int maxYear) {
        List<String> yearsList = new ArrayList<>();
        int currYear = firstYear;
        while (currYear <= maxYear) {
            yearsList.add(String.valueOf(currYear++));
        }
        Collections.reverse(yearsList);
        return yearsList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_class);
        db = AppDatabase.singleton(this);
        enteredCourses = db.coursesDao().getForStudent(studentId);

        // prevent software keyboard from messing up with the layout
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // dropdown for quarter
        quarterSpinner = (Spinner) findViewById(R.id.quarter_spinner);
        ArrayAdapter<CharSequence> quarterAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, quarters);
        quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterSpinner.setAdapter(quarterAdapter);

        // dropdown for year
        yearSpinner = (Spinner) findViewById(R.id.year_spinner);
        ArrayAdapter<CharSequence> yearAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, yearsList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        yearSpinner.setSelection(1, true); // Starts by selecting the last year, not the first

        // recycle view for courses
        coursesRecyclerView = findViewById(R.id.courses_recycler_view);
        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        coursesViewAdapter = new CoursesViewAdapter(enteredCourses, (course) -> {
            db.coursesDao().delete(course);
        });
        coursesRecyclerView.setAdapter(coursesViewAdapter);
    }

    public void onEnterClicked(View view) {
        // retrieve the user inputs and convert them to strings
        TextView subjectView = (TextView) findViewById(R.id.course_subject_textview);
        TextView numberView = (TextView) findViewById(R.id.course_number_textview);
        String courseSubject = subjectView.getText().toString().toUpperCase();
        String courseNumber = numberView.getText().toString().toUpperCase();
        String courseQuarter = (String) quarterSpinner.getSelectedItem();
        String courseYear = (String) yearSpinner.getSelectedItem();

        // if any of the entries is empty, show an error message
        if (courseSubject.isEmpty() || courseNumber.isEmpty() ||
                courseQuarter.isEmpty() || courseYear.isEmpty()){
            Utilities.showAlert(this, "Please fill in every field.");
            return;
        }

        // Check that course subject is a string of three or four letters
        if (!(courseSubject.length() <= 4 && courseSubject.length() >= 2) ||
                !(courseSubject.matches("[a-zA-Z]+"))) {
            Utilities.showAlert(this, "Please enter a three or four letter subject name.");
            return;
        }

        // Check that course number is a string of one to four numbers
        if ((courseNumber.length() > 4) ||
                !((courseNumber.length() == 4 && courseNumber.matches("[0-9][0-9][0-9][a-zA-Z]")) || // e.g. 100A
                        (courseNumber.length() == 3 && courseNumber.matches("[0-9][0-9][a-zA-Z]")) || //e.g. 15L
                        (courseNumber.length() == 2 && courseNumber.matches("[0-9][a-zA-Z]")) || // e.g. 1A
                        (courseNumber.length() <= 3 && courseNumber.matches("[0-9]+")))  // e.g. 100, 12
        ) {
            Utilities.showAlert(this, "Please enter a valid course number. \n(e.g. 100 or 15L)");
            return;
        }

        // create a new course string
        String courseEntry = courseSubject + " " + courseNumber + " " + courseQuarter + " "+ courseYear;

        // check if the course is already entered
        // if so, show an alert and return
        for (Course c : enteredCourses){
            if (c.name.equals(courseEntry)){
                Utilities.showAlert(this, "This course is already entered");
                return;
            }
        }

        // create a new course and update the list
        int newId = db.coursesDao().count() + 1;
        Course newCourse = new Course(newId, studentId, courseEntry);
        db.coursesDao().insert(newCourse);
        coursesViewAdapter.addCourse(newCourse);
    }

    public void onFinishClicked(View view) {
        // if no course is entered, show alert and return
        if (enteredCourses.isEmpty()){
            Utilities.showAlert(this, "Please enter at least one course.");
            return;
        }

        // go back to the main activity
        finish();
    }
}