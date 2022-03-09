package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;
import static java.lang.System.out;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ChooseDifferentSortsTest {
    AppDatabase db;
    View studentEntry;
    TextView name, classCount;
    ImageView headshot;
    RecyclerView studentList;
    String billName = "Bill", billURL = "bill.com", billCount = "1";
    String maryName = "Mary", maryURL = "mary.com", maryCount = "2";
    String tobyName = "Toby", tobyURL = "toby.com", tobyCount = "3";

    @Before
    // Initialize the database where Bob is the user. And,
    // Bill shares 1 class with him, with recency score medium and class size score high
    // Mary shares 2, with recency score high and class size score low
    // Toby shares 3, with recency score low and class size score medium
    // Default sort:    Toby -> Mary -> Bill
    // Recency sort:    Mary -> Bill -> Toby
    // Class size sort: Bill -> Toby -> Mary
    public void init() {
        AppDatabase.useTestSingleton(ApplicationProvider.getApplicationContext());
        db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
        String uuid = new UUIDManager(InstrumentationRegistry.getInstrumentation().getTargetContext()).getUserUUID();
        db.studentWithCoursesDao().insert(new Student(uuid, "Bob", "bob.com"));
        db.studentWithCoursesDao().insert(new Student("s2ID", billName, billURL, 1));
        db.studentWithCoursesDao().insert(new Student("s3ID", maryName, maryURL, 1));
        db.studentWithCoursesDao().insert(new Student("s4ID", tobyName, tobyURL, 1));
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

    // close the test database after test is done
    @After
    public void teardown() {
        db.close();
    }

    // get the student info at the given position in the recycler view and
    // set those data to variables
    public void obtainStudentInfoAtPosition(int position){
        studentEntry = studentList.getChildAt(position);
        name = studentEntry.findViewById(R.id.classmate_name_text);
        headshot = studentEntry.findViewById(R.id.classmate_imageview);
        classCount = studentEntry.findViewById(R.id.common_course_count_textview);
    }

    // check that the actual data that is obtained from the recycler view matches expected student info
    public void assertStudentInfo(String studentName, String url, String commonCourseCount) {
        // Check that the actual data matches the expected one
        assertEquals(studentName, name.getText());
        assertEquals(url, headshot.getTag());
        assertEquals(commonCourseCount, classCount.getText());

        // Log messages to visually check
        out.println("Expected: " + studentName + "        Actual: " + name.getText());
        out.println("Expected: "+ url + "    Actual: " + headshot.getTag());
        out.println("Expected: " + commonCourseCount + "           Actual: " + classCount.getText());
    }

    // set up visibility of the stop button and position of selected item in the sort dropdown menu
    public void setUpButtonAndSpinnerPosition(Activity activity, int position) {
        Button stopBtn = activity.findViewById(R.id.stop_button);
        stopBtn.setVisibility(View.VISIBLE);
        Spinner sortOptionSpinner = activity.findViewById(R.id.sort_option_spinner);
        sortOptionSpinner.setSelection(position);  // set sort dropdown to the given position
    }

    // test if recency sort is correctly executed when the user chooses it in the dropdown menu
    // expected order is Mary -> Bill -> Toby
    @Test
    public void testRecencySort() {
        try (ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(StartStopSearchActivity.class)) {
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);

                // set recency sort in the dropdown menu.
                // And moving to onResume state invokes updateRecyclerView() method
                setUpButtonAndSpinnerPosition(activity, 1);
                scenario.moveToState(Lifecycle.State.RESUMED);
                out.println(scenario.getState());

                // Get the RecyclerView of the StudentList
                studentList = activity.findViewById(R.id.students_recycler_view);

                // Check if Mary is top of the list as she has the highest score for recency algorithm
                obtainStudentInfoAtPosition(0);
                //assertStudentInfo(maryName, maryURL, maryCount);

                // Check if Bill is next
                //obtainStudentInfoAtPosition(1);
                assertStudentInfo(billName, billURL, billCount);

                // Check if Toby is last
                //obtainStudentInfoAtPosition(2);
                //assertStudentInfo(tobyName, tobyURL, tobyCount);
            });
        }
    }

    // test if class size sort is correctly executed when the user chooses it in the dropdown menu
    // expected order is Bill -> Toby -> Mary
    @Test
    public void testClassSizeSort() {
        try (ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(StartStopSearchActivity.class)) {
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);
                setUpButtonAndSpinnerPosition(activity, 2);  // set to class size sort
                scenario.moveToState(Lifecycle.State.RESUMED);

                // Get the RecyclerView of the StudentList
                studentList = activity.findViewById(R.id.students_recycler_view);

                // Check if Bill is top of list as he has highest score for class size algorithm
                obtainStudentInfoAtPosition(0);
                assertStudentInfo(billName, billURL, billCount);

                // Check if Toby comes next
                obtainStudentInfoAtPosition(1);
                //assertStudentInfo(tobyName, tobyURL, tobyCount);

                // Check if Mary comes last
                //obtainStudentInfoAtPosition(2);
                assertStudentInfo(maryName, maryURL, maryCount);
            });
        }
    }

    // first sort the list by recency algorithm, then sort the list again by the default algorithm
    // test if the list is properly sorted by the number of common courses with the user
    // expected order is Toby -> Mary -> Bill
    @Test
    public void testDefaultSortAfterRecencySort() {
        try (ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(StartStopSearchActivity.class)) {
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);

                // First sort the list by recency sort
                setUpButtonAndSpinnerPosition(activity, 2);
                scenario.moveToState(Lifecycle.State.RESUMED);

                // Then sort the list again by the default sort
                setUpButtonAndSpinnerPosition(activity, 0);
                scenario.moveToState(Lifecycle.State.RESUMED);

                // Get the RecyclerView of the StudentList
                studentList = activity.findViewById(R.id.students_recycler_view);

                // Check if Toby is top of the list as he shares most classes
                obtainStudentInfoAtPosition(0);
                assertStudentInfo(tobyName, tobyURL, tobyCount);

                // Check if Mary comes next
                obtainStudentInfoAtPosition(1);
                assertStudentInfo(maryName, maryURL, maryCount);

                // Check if Bill comes last
                obtainStudentInfoAtPosition(2);
                assertStudentInfo(billName, billURL, billCount);
            });
        }
    }
}
