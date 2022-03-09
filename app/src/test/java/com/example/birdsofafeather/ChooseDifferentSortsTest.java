package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;
import static java.lang.System.out;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.Session;
import com.example.birdsofafeather.models.db.Student;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ChooseDifferentSortsTest {

    @Before
    // Initialize the database where Bob is the user. And,
    // Bill shares 1 class with him, with recency score medium and class size score high
    // Mary shares 2, with recency score high and class size score low
    // Toby shares 3, with recency score low and class size score medium
    // Default sort: Toby -> Mary -> Bill
    // Recency: Mary -> Bill -> Toby
    // Class size: Bill -> Toby -> Mary
    public void init() {
        AppDatabase.useTestSingleton(ApplicationProvider.getApplicationContext());
        AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
        String uuid = new UUIDManager(InstrumentationRegistry.getInstrumentation().getTargetContext()).getUserUUID();
        db.studentWithCoursesDao().insert(new Student(uuid, "Bob", "bob.com"));
        db.studentWithCoursesDao().insert(new Student("s2ID", "Bill", "bill.com", 1));
        db.studentWithCoursesDao().insert(new Student("s3ID", "Mary", "mary.com", 1));
        db.studentWithCoursesDao().insert(new Student("s4ID", "Toby", "toby.com", 1));
        // add dummy session
        db.sessionWithStudentsDao().insert(new Session("dummy"));

        // Bob's classes
        db.coursesDao().insert(new Course(uuid, "CSE 8A FA 2018 Large"));
        db.coursesDao().insert(new Course(uuid, "CSE 8B WI 2018 Large"));
        db.coursesDao().insert(new Course(uuid, "CSE 15L WI 2018 Huge"));
        db.coursesDao().insert(new Course(uuid, "CSE 190 FA 2021 Tiny"));
        db.coursesDao().insert(new Course(uuid, "CSE 110 WI 2022 Huge"));

        // Bill's classes
        db.coursesDao().insert(new Course("s2ID", "CSE 190 FA 2021 Tiny"));

        // Mary's classes
        db.coursesDao().insert(new Course("s3ID", "CSE 8A FA 2018 Large"));
        db.coursesDao().insert(new Course("s3ID", "CSE 110 WI 2022 Huge"));

        // Toby's classes
        db.coursesDao().insert(new Course("s4ID", "CSE 8A FA 2018 Large"));
        db.coursesDao().insert(new Course("s4ID", "CSE 8B WI 2018 Large"));
        db.coursesDao().insert(new Course("s4ID", "CSE 15L WI 2018 Huge"));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext());
        preferences.edit().putInt("sessionId", 1).commit();
    }

    @Test
    public void defaultSortTest() {
        try (ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(StartStopSearchActivity.class)) {
            scenario.onActivity(activity -> {
                // Hack to call updateRecyclerView() to populate recycler view with values in database
                scenario.moveToState(Lifecycle.State.CREATED);
                Button stopBtn = activity.findViewById(R.id.stop_button);
                stopBtn.setVisibility(View.VISIBLE);
                Spinner sortOptionSpinner = activity.findViewById(R.id.sort_option_spinner);
                sortOptionSpinner.setSelection(0);  // set to the default sort
                scenario.moveToState(Lifecycle.State.RESUMED);
                out.println(scenario.getState());

                // Get the RecyclerView of the StudentList
                RecyclerView studentList = activity.findViewById(R.id.students_recycler_view);
                final int studentCount = studentList.getChildCount();

                // There are 3 other students in database, but only two should appear in the view
                assertEquals(3, studentCount);

                // Check if Toby is top of the list as he shares most classes
                View studentEntry = studentList.getChildAt(0);
                TextView name = studentEntry.findViewById(R.id.classmate_name_text);
                ImageView headshot = studentEntry.findViewById(R.id.classmate_imageview);
                TextView classCount = studentEntry.findViewById(R.id.common_course_count_textview);

                // Check if Toby information is the same as expected
                assertEquals("Toby", name.getText());
                assertEquals("toby.com", headshot.getTag());
                assertEquals("3", classCount.getText());

                // Log messages to visually check
                out.println("Expected: Toby        Actual: " + name.getText());
                out.println("Expected: toby.com    Actual: " + headshot.getTag());
                out.println("Expected: 3           Actual: " + classCount.getText());

                // Check if Mary comes next
                studentEntry = studentList.getChildAt(1);
                name = studentEntry.findViewById(R.id.classmate_name_text);
                headshot = studentEntry.findViewById(R.id.classmate_imageview);
                classCount = studentEntry.findViewById(R.id.common_course_count_textview);

                // Check if Mary and her information is the same as expected
                assertEquals("Mary", name.getText());
                assertEquals("mary.com", headshot.getTag());
                assertEquals("2", classCount.getText());

                // Log messages to visually check
                out.println("Expected: Mary        Actual: " + name.getText());
                out.println("Expected: mary.com    Actual: " + headshot.getTag());
                out.println("Expected: 2           Actual: " + classCount.getText());

                // Check if Bill comes last
                studentEntry = studentList.getChildAt(2);
                name = studentEntry.findViewById(R.id.classmate_name_text);
                headshot = studentEntry.findViewById(R.id.classmate_imageview);
                classCount = studentEntry.findViewById(R.id.common_course_count_textview);

                // Check if Bill and his information is the same as expected
                assertEquals("Bill", name.getText());
                assertEquals("bill.com", headshot.getTag());
                assertEquals("1", classCount.getText());

                out.println("Expected: Bill        Actual: " + name.getText());
                out.println("Expected: bill.com    Actual: " + headshot.getTag());
                out.println("Expected: 1           Actual: " + classCount.getText());
            });
        }
    }

    @Test
    public void recencySortTest() {
        try (ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(StartStopSearchActivity.class)) {
            scenario.onActivity(activity -> {
                // Hack to call updateRecyclerView() to populate recycler view with values in database
                scenario.moveToState(Lifecycle.State.CREATED);
                Button stopBtn = activity.findViewById(R.id.stop_button);
                stopBtn.setVisibility(View.VISIBLE);
                Spinner sortOptionSpinner = activity.findViewById(R.id.sort_option_spinner);
                sortOptionSpinner.setSelection(1);  // set to the recency sort
                scenario.moveToState(Lifecycle.State.RESUMED);
                out.println(scenario.getState());

                // Get the RecyclerView of the StudentList
                RecyclerView studentList = activity.findViewById(R.id.students_recycler_view);
                final int studentCount = studentList.getChildCount();

                // Check if Mary is top of the list as she has the highest score for recency algorithm
                View studentEntry = studentList.getChildAt(0);
                TextView name = studentEntry.findViewById(R.id.classmate_name_text);
                ImageView headshot = studentEntry.findViewById(R.id.classmate_imageview);
                TextView classCount = studentEntry.findViewById(R.id.common_course_count_textview);

                // Check if Mary and her information is the same as expected
                assertEquals("Mary", name.getText());
                assertEquals("mary.com", headshot.getTag());
                assertEquals("2", classCount.getText());

                // Log messages to visually check
                out.println("Expected: Mary        Actual: " + name.getText());
                out.println("Expected: mary.com    Actual: " + headshot.getTag());
                out.println("Expected: 2           Actual: " + classCount.getText());

                // Check if Bill is next
                studentEntry = studentList.getChildAt(1);
                name = studentEntry.findViewById(R.id.classmate_name_text);
                headshot = studentEntry.findViewById(R.id.classmate_imageview);
                classCount = studentEntry.findViewById(R.id.common_course_count_textview);

                // Check if Bill and his information is the same as expected
                assertEquals("Bill", name.getText());
                assertEquals("bill.com", headshot.getTag());
                assertEquals("1", classCount.getText());

                out.println("Expected: Bill        Actual: " + name.getText());
                out.println("Expected: bill.com    Actual: " + headshot.getTag());
                out.println("Expected: 1           Actual: " + classCount.getText());

                // Check if Toby is last
                studentEntry = studentList.getChildAt(2);
                name = studentEntry.findViewById(R.id.classmate_name_text);
                headshot = studentEntry.findViewById(R.id.classmate_imageview);
                classCount = studentEntry.findViewById(R.id.common_course_count_textview);

                // Check if Toby and his information is the same as expected
                assertEquals("Toby", name.getText());
                assertEquals("toby.com", headshot.getTag());
                assertEquals("3", classCount.getText());

                // Log messages to visually check
                out.println("Expected: Toby        Actual: " + name.getText());
                out.println("Expected: toby.com    Actual: " + headshot.getTag());
                out.println("Expected: 3           Actual: " + classCount.getText());
            });
        }
    }

    @Test
    public void classSizeSortTest() {
        try (ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(StartStopSearchActivity.class)) {
            scenario.onActivity(activity -> {
                // Hack to call updateRecyclerView() to populate recycler view with values in database
                scenario.moveToState(Lifecycle.State.CREATED);
                Button stopBtn = activity.findViewById(R.id.stop_button);
                stopBtn.setVisibility(View.VISIBLE);
                Spinner sortOptionSpinner = activity.findViewById(R.id.sort_option_spinner);
                sortOptionSpinner.setSelection(2);  // set to the class size sort
                scenario.moveToState(Lifecycle.State.RESUMED);
                out.println(scenario.getState());

                // Get the RecyclerView of the StudentList
                RecyclerView studentList = activity.findViewById(R.id.students_recycler_view);
                final int studentCount = studentList.getChildCount();

                // Check if Bill is top if list as he has highest score for class size algorithm
                View studentEntry = studentList.getChildAt(0);
                TextView name = studentEntry.findViewById(R.id.classmate_name_text);
                ImageView headshot = studentEntry.findViewById(R.id.classmate_imageview);
                TextView classCount = studentEntry.findViewById(R.id.common_course_count_textview);

                // Check if Bill and his information is the same as expected
                assertEquals("Bill", name.getText());
                assertEquals("bill.com", headshot.getTag());
                assertEquals("1", classCount.getText());

                out.println("Expected: Bill        Actual: " + name.getText());
                out.println("Expected: bill.com    Actual: " + headshot.getTag());
                out.println("Expected: 1           Actual: " + classCount.getText());

                // Check if Toby comes next
                studentEntry = studentList.getChildAt(1);
                name = studentEntry.findViewById(R.id.classmate_name_text);
                headshot = studentEntry.findViewById(R.id.classmate_imageview);
                classCount = studentEntry.findViewById(R.id.common_course_count_textview);

                // Check if Toby and his information is the same as expected
                assertEquals("Toby", name.getText());
                assertEquals("toby.com", headshot.getTag());
                assertEquals("3", classCount.getText());

                // Log messages to visually check
                out.println("Expected: Toby        Actual: " + name.getText());
                out.println("Expected: toby.com    Actual: " + headshot.getTag());
                out.println("Expected: 3           Actual: " + classCount.getText());

                // Check if Mary comes last
                studentEntry = studentList.getChildAt(2);
                name = studentEntry.findViewById(R.id.classmate_name_text);
                headshot = studentEntry.findViewById(R.id.classmate_imageview);
                classCount = studentEntry.findViewById(R.id.common_course_count_textview);

                // Check if Mary and her information is the same as expected
                assertEquals("Mary", name.getText());
                assertEquals("mary.com", headshot.getTag());
                assertEquals("2", classCount.getText());

                // Log messages to visually check
                out.println("Expected: Mary        Actual: " + name.getText());
                out.println("Expected: mary.com    Actual: " + headshot.getTag());
                out.println("Expected: 2           Actual: " + classCount.getText());
            });
        }
    }
}
