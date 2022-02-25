package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static java.lang.System.out;

import android.view.View;
import android.widget.CheckBox;
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
public class SeeClassmateListTest {

    @Before
    // Initialize the database where Bob is the user, Bill shares 1 class with him, Mary shares 2,
    // and Toby shares none.
    public void init(){
        AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
        db.studentWithCoursesDao().insert(new Student("Bob", "bob.com"));
        db.studentWithCoursesDao().insert(new Student("Bill", "bill.com"));
        db.studentWithCoursesDao().insert(new Student("Mary", "mary.com", true));
        db.studentWithCoursesDao().insert(new Student("Toby", "toby.com"));
        // Bob's classes
        db.coursesDao().insert(new Course(1, "CSE 20 FA 2021")) ;
        db.coursesDao().insert(new Course(1, "CSE 100 FA 2021")) ;

        // Bill's class (Has 2, shares 1)
        db.coursesDao().insert(new Course(2, "CSE 20 FA 2021")) ;
        db.coursesDao().insert(new Course(2, "CSE 15L FA 2021")) ;

        // Mary's classes (Has 2, shares 2)
        db.coursesDao().insert(new Course(3, "CSE 20 FA 2021")) ;
        db.coursesDao().insert(new Course(3, "CSE 100 FA 2021")) ;

        // Toby's class (Has 1, shares none)
        db.coursesDao().insert(new Course(4, "CSE 8B FA 2021")) ;
    }

    @Test
    // Test to make sure a students' entries shows up correctly on the search page.
    public void testViewingList(){
        try(ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(StartStopSearchActivity.class)){
            scenario.onActivity(activity -> {
                // Get the RecyclerView of the StudentList
                RecyclerView studentList = activity.findViewById(R.id.students_recycler_view);
                final int studentCount = studentList.getChildCount();

                // There are 3 other students in database, but only two should appear in the view
                assertEquals(2, studentCount);

                // Assert Mary and her information was rendered first because she shares more classes
                View studentEntry = studentList.getChildAt(0);
                TextView name = studentEntry.findViewById(R.id.classmate_name_text);
                ImageView headshot = studentEntry.findViewById(R.id.classmate_imageview);
                TextView classCount = studentEntry.findViewById(R.id.common_course_count_textview);

                // Check if Mary and her information is the same as expected
                assertEquals("Mary", name.getText());
                assertEquals("mary.com", headshot.getTag());
                assertEquals("2", classCount.getText());

                // Test if the Favorite Checkbox is checked
                CheckBox favoriteIcon = studentEntry.findViewById(R.id.favorite);
                assertTrue(favoriteIcon.isChecked());

                // Log messages to visually check
                out.println("Expected: Mary        Actual: " + name.getText());
                out.println("Expected: mary.com    Actual: " + headshot.getTag());
                out.println("Expected: 2           Actual: " + classCount.getText());
                out.println("Expected: True        Actual: " + favoriteIcon.isChecked());

                // Assert Bill's info was rendered next correctly
                studentEntry = studentList.getChildAt(1);
                name = studentEntry.findViewById(R.id.classmate_name_text);
                headshot = studentEntry.findViewById(R.id.classmate_imageview);
                classCount = studentEntry.findViewById(R.id.common_course_count_textview);

                // Check if Bill and his information is the same as expected
                assertEquals("Bill", name.getText());
                assertEquals("bill.com", headshot.getTag());
                assertEquals("1", classCount.getText());

                // Check if the Favorite Checkbox is not checked
                favoriteIcon = studentEntry.findViewById(R.id.favorite);
                assertFalse(favoriteIcon.isChecked());

                out.println("Expected: Bill        Actual: " + name.getText());
                out.println("Expected: bill.com    Actual: " + headshot.getTag());
                out.println("Expected: 1           Actual: " + classCount.getText());
                out.println("Expected: False        Actual: " + favoriteIcon.isChecked());
            });
        }
    }
}