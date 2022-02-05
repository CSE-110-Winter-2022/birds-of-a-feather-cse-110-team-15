package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;
import java.util.Vector;

public class EnterCourseActivity extends AppCompatActivity {
    Spinner quarterSpinner, yearSpinner;
    ArrayList<String> enteredCourseList = new ArrayList<>();
    CourseViewAdapter listAdapter;
    AppDatabase db;
    int studentId = 0; // for now set student id to 0

    String[] quarters = {"FA", "WI", "SP", "SS1", "SS2", "SSS"};
    String[] years = {"2022", "2021", "2020","2019","2018","2017","2016","2015","2014","2013"};

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
        return yearsList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_class);
        ListView courseListView = (ListView) findViewById(R.id.entered_class_list_view);
        db = AppDatabase.singleton(this);

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
        yearSpinner.setSelection(-1, true); // Starts by selecting the last year, not the first

        // list of courses entered
        listAdapter = new CourseViewAdapter(this, enteredCourseList);
        courseListView.setAdapter(listAdapter);
    }


    public void onEnterClicked(View view) {
        // retrieve the user inputs and convert them to strings
        TextView subjectView = (TextView) findViewById(R.id.course_subject_textview);
        TextView numberView = (TextView) findViewById(R.id.course_number_textview);
        String courseSubject = subjectView.getText().toString().toUpperCase();
        String courseNumber = numberView.getText().toString();
        String courseQuarter = (String) quarterSpinner.getSelectedItem();
        String courseYear = (String) yearSpinner.getSelectedItem();

        // create a string to show as a list in the page
        String courseEntry = courseSubject + " " + courseNumber + " " + courseQuarter + " "+ courseYear;

        // if any of the entries is empty, show an error message
        if (courseSubject.isEmpty() || courseNumber.isEmpty() ||
        courseQuarter.isEmpty() || courseYear.isEmpty()){
            Utilities.showAlert(this, "Please fill in every field.");
            return;
        }

        // Check that course subject is a string of three or four letters
        if (!(courseSubject.length() == 4 || courseSubject.length() == 3) ||
                !(courseSubject.matches("[a-zA-Z]+"))) {
            Utilities.showAlert(this, "Please enter a three or four letter subject name.");
            return;
        }

        // Check that course number is a string of one to four numbers
        if ((courseNumber.length() > 3) ||
                !(courseNumber.matches("[0-9]+"))) {
            Utilities.showAlert(this, "Please enter a valid 1-3 digit course number.");
            return;
        }

        // if the course is already entered, show an error message
        if (enteredCourseList.contains(courseEntry)){
            Utilities.showAlert(this, "This course is already entered");
            return;
        }

        // update course list
        enteredCourseList.add(courseEntry);
        listAdapter.notifyDataSetChanged();
    }

    public void onFinishClicked(View view) {
        // if no course is entered, show alert and return
        if (enteredCourseList.isEmpty()){
            Utilities.showAlert(this, "Please enter at least one course.");
            return;
        }

        // record courses from the arraylist to the database one by one
        // note: a user is creating a new profile, so i assumed course ids would be 1,2,...
        // but honestly i'm not sure how this works yet
        // also i might refactor the code so that the listview would display the date from
        // the database, instead of local arraylist that i was using
        for (int count = 0; count < enteredCourseList.size(); count++){
            Course newCourse = new Course(count + 1, studentId, enteredCourseList.get(count));
            db.coursesDao().insert(newCourse);
        }

        // go to the next activity?

    }
}