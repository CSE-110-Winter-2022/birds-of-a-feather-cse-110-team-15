package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class EnterClassActivity extends AppCompatActivity {
    Spinner quarterSpinner, yearSpinner;
    ArrayList<String> enteredCourseList=new ArrayList<String>();
    CourseViewAdapter listAdapter;

    String[] quarters = {"FA", "WI", "SP", "SS1", "SS2", "SSS"};
    String[] years = {"2022", "2021", "2020","2019","2018","2017","2016","2015","2014","2013"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_class);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ListView courseListView = (ListView) findViewById(R.id.entered_class_list_view);

        // dropdown for quarter
        quarterSpinner = (Spinner) findViewById(R.id.quarter_spinner);
        ArrayAdapter<CharSequence> quarterAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, quarters);
        quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterSpinner.setAdapter(quarterAdapter);

        // dropdown for year
        yearSpinner = (Spinner) findViewById(R.id.year_spinner);
        ArrayAdapter<CharSequence> yearAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

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

        // if any of the entries is empty, show an error message
        if (courseSubject.isEmpty() || courseNumber.isEmpty() ||
        courseQuarter.isEmpty() || courseYear.isEmpty()){
            // TODO
            return;
        }

        // create a string to show as a list in the page
        String courseEntry = courseSubject + " " + courseNumber + " " + courseQuarter + " "+ courseYear;

        // if the course is already entered, show an error message
        if (enteredCourseList.contains(courseEntry)){
            // TODO
            return;
        }

        // update course list
        enteredCourseList.add(courseEntry);
        listAdapter.notifyDataSetChanged();
    }

    public void onFinishClicked(View view) {
    }
}