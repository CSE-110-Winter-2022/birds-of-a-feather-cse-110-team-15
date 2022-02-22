package com.example.birdsofafeather;


import static org.junit.Assert.assertEquals;

import static java.lang.System.out;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.Student;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FavoriteStudentTest {

    @Before
    // Initialize a database where Bob is the user, Bill shares 1 class with him and Mary shares 2
    public void init(){
        AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
        Student s1 = new Student("Bob", "bob.com");
        Student s2 = new Student("Bill", "bill.com");
        Student s3 = new Student("Mary", "mary.com");
        db.studentWithCoursesDao().insert(s1);
        db.studentWithCoursesDao().insert(s2);
        db.studentWithCoursesDao().insert(s3);
        Course c1 = new Course(1, "CSE 20 FA 2021");
        Course c2 = new Course(1, "CSE 100 FA 2021");
        Course c3 = new Course(2, "CSE 20 FA 2021");
        Course c4 = new Course(3, "CSE 20 FA 2021");
        Course c5 = new Course(3, "CSE 100 FA 2021");
        db.coursesDao().insert(c1);
        db.coursesDao().insert(c2);
        db.coursesDao().insert(c3);
        db.coursesDao().insert(c4);
        db.coursesDao().insert(c5);
    }

    @Test
    // Simple test to make sure a student's entry shows up correctly on the search page.
    public void testViewingList(){
        try(ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(StartStopSearchActivity.class)){
            scenario.onActivity(activity -> {
                RecyclerView studentList = activity.findViewById(R.id.students_recycler_view);
                final int studentCount = studentList.getChildCount();
                // Only two other students in database, only two should appear in the view
                assertEquals(2, studentCount);

                // Assert Mary and her information was rendered first because she shares more classes
                View studentEntry = studentList.getChildAt(0);
                TextView name = studentEntry.findViewById(R.id.classmate_name_text);
                ImageView headshot = studentEntry.findViewById(R.id.classmate_imageview);
                TextView classCount = studentEntry.findViewById(R.id.common_course_count_textview);
                assertEquals("Mary", name.getText());
                assertEquals("mary.com", headshot.getTag());
                assertEquals("2", classCount.getText());

                // Don't actually know if this is how the log messages should be printed or if
                // it needs to be more elaborate
                out.println("Expected: Mary        Actual: " + name.getText());
                out.println("Expected: mary.com    Actual: " + headshot.getTag());
                out.println("Expected: 2           Actual: " + classCount.getText());


                // Assert Bill's info was rendered next correctly
                studentEntry = studentList.getChildAt(1);
                name = studentEntry.findViewById(R.id.classmate_name_text);
                headshot = studentEntry.findViewById(R.id.classmate_imageview);
                classCount = studentEntry.findViewById(R.id.common_course_count_textview);
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
    public void testFavoriteStudentsFromList(){
        try(ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(StartStopSearchActivity.class)){
            scenario.onActivity(activity -> {
                RecyclerView studentList = activity.findViewById(R.id.students_recycler_view);

                // This is how we get the entry at a specific index in the RecyclerView
                View studentEntry = studentList.getChildAt(0);
                final int studentCount = studentList.getChildCount();
            });
        }
    }

}
