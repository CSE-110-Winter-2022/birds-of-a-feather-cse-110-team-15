package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class EnterClassActivity extends AppCompatActivity {
    Spinner quarterSpinner, yearSpinner;
    ArrayList<String> enteredCourseList=new ArrayList<String>();
    ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_class);
        ListView courseListView = (ListView) findViewById(R.id.entered_class_list_view);


        // dropdown for quarter
        quarterSpinner = (Spinner) findViewById(R.id.quarter_spinner);
        ArrayAdapter<CharSequence> quarterAdapter = ArrayAdapter.createFromResource(this,
                R.array.quarter_array, android.R.layout.simple_spinner_item);
        quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterSpinner.setAdapter(quarterAdapter);

        // dropdown for year
        yearSpinner = (Spinner) findViewById(R.id.year_spinner);
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // list of courses entered
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, enteredCourseList);
        courseListView.setAdapter(listAdapter);
    }


    public void onEnterClicked(View view) {
        // retrieve the user inputs from EditTexts and Spinners
        TextView subjectView = (TextView) findViewById(R.id.course_subject_textview);
        TextView numberView = (TextView) findViewById(R.id.course_number_textview);
        String courseSubject = subjectView.getText().toString();
        String courseNumber = numberView.getText().toString();
        String courseQuarter = (String) quarterSpinner.getSelectedItem();
        String courseYear = (String) yearSpinner.getSelectedItem();

        if (courseSubject.isEmpty() || courseNumber.isEmpty() ||
        courseQuarter.isEmpty() || courseYear.isEmpty()){
            // TODO
            return;
        }


        String courseEntry = courseSubject + " " + courseNumber + " " + courseQuarter + " "+ courseYear;

        enteredCourseList.add(courseEntry);
        listAdapter.notifyDataSetChanged();
    }

    public void onFinishClicked(View view) {
    }
}