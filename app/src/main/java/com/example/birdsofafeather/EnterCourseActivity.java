package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;

import java.util.ArrayList;
import java.util.List;

public class EnterCourseActivity extends AppCompatActivity {
    Spinner quarterSpinner, yearSpinner;
    List<Course> enteredCourses;
    AppDatabase db;
    String studentId; // User's obtained from UUIDManager

    private RecyclerView coursesRecyclerView;
    private RecyclerView.LayoutManager coursesLayoutManager;
    private CoursesViewAdapter coursesViewAdapter;

    String[] quarters = {"FA", "WI", "SP", "SS1", "SS2", "SSS"};

    // firstYear: 2016
    // maxYear: In a full release, could be replaced with call for current year.
    List<String> yearsList = makeYearsList(2016, 2022);

    // Take beginning and end years we want to support, create list of all years in between them
    private List<String> makeYearsList(int firstYear, int maxYear) {
        List<String> yearsList = new ArrayList<>();
        for (int i = maxYear; i >= firstYear; i--) {
            yearsList.add(String.valueOf(i));
        }
        return yearsList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_class);
        //get the intent for extra student_id
        Bundle extras = getIntent().getExtras();
        studentId = new UUIDManager(getApplicationContext()).getUserUUID();

        db = AppDatabase.singleton(this);
        enteredCourses = db.coursesDao().getForStudent(studentId);

        // prevent software keyboard from messing up with the layout
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // dropdown for quarter
        quarterSpinner = (Spinner) findViewById(R.id.quarter_spinner);
        ArrayAdapter<String> quarterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, quarters);
        quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterSpinner.setAdapter(quarterAdapter);

        // dropdown for year
        yearSpinner = (Spinner) findViewById(R.id.year_spinner);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearsList);
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
                courseQuarter.isEmpty() || courseYear.isEmpty()) {
            Utilities.showAlert(this, getString(R.string.empty_field_err_str));
            return;
        }

        // Check that course subject is a string of two to four letters
        if ( !(courseSubject.matches("[a-zA-Z]{2,4}"))) {
            Utilities.showAlert(this, getString(R.string.course_name_invalid_err_str));
            return;
        }

        // Check that course number is a valid string of one to three numbers and an optional letter
        // Regex: 1-3 numerical digits followed by 0-1 letter
        if ( !courseNumber.matches("[1-9][0-9]{0,2}[a-zA-Z]?(H)?") ) {
            Utilities.showAlert(this, getString(R.string.course_number_invalid_err_str));
            return;
        }

        // create a new course string
        String courseEntry = String.join(" ", courseSubject, courseNumber, courseQuarter, courseYear);

        // check if the course is already entered
        // if so, show an alert and return
        for (Course c : enteredCourses){
            if (c.getName().equals(courseEntry)){
                Utilities.showAlert(this, getString(R.string.duplicate_course_err_str));
                return;
            }
        }

        // create a new course and update the list
        Course newCourse = new Course(studentId, courseEntry);
        int course_id = (int) db.coursesDao().insert(newCourse);
        newCourse.setCourseId(course_id); // set newly created id
        Log.d("", "" + newCourse.getCourseId());
        coursesViewAdapter.addCourse(newCourse);
    }

    public void onFinishClicked(View view) {
        // if no course is entered, show alert and return
        if (enteredCourses.isEmpty()){
            Utilities.showAlert(this, getString(R.string.no_course_entered_err_str));
            return;
        }

        // go to StartStopSearchActivity
        Intent intent = new Intent(this, StartStopSearchActivity.class);
        startActivity(intent);
        finish();
    }
}