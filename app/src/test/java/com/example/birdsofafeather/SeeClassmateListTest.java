package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static java.lang.System.out;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.birdsofafeather.models.db.StudentWithCourses;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SeeClassmateListTest {

    @Before
    // Initialize the database where Bob is the user, Bill shares 1 class with him, Mary shares 2,
    // and Toby shares none.
    public void init(){
        AppDatabase.useTestSingleton(ApplicationProvider.getApplicationContext());
        AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
        String uuid = new UUIDManager(InstrumentationRegistry.getInstrumentation().getTargetContext()).getUserUUID();
        db.studentWithCoursesDao().insert(new Student(uuid,"Bob", "bob.com"));
        db.studentWithCoursesDao().insert(new Student("s2ID", "Bill", "bill.com", 1, false));
        db.studentWithCoursesDao().insert(new Student("s3ID", "Mary", "mary.com", 1, true, true));
        db.studentWithCoursesDao().insert(new Student("s4ID", "Toby", "toby.com", 1));

        // add dummy session
        db.sessionWithStudentsDao().insert(new Session("dummy"));

        // Bob's classes
        db.coursesDao().insert(new Course(uuid, "CSE 20 FA 2021 Large")) ;
        db.coursesDao().insert(new Course(uuid, "CSE 100 FA 2021 Large")) ;
        db.coursesDao().insert(new Course(uuid, "CSE 120 FA 2021 Medium")) ;

        // Bill's class (Has 2, shares 1)
        db.coursesDao().insert(new Course("s2ID", "CSE 20 FA 2021 Large")) ;
        db.coursesDao().insert(new Course("s2ID", "CSE 15L FA 2021 Gigantic")) ;

        // Mary's classes (Has 2, shares 2)
        db.coursesDao().insert(new Course("s3ID", "CSE 20 FA 2021 Large")) ;
        db.coursesDao().insert(new Course("s3ID", "CSE 100 FA 2021 Large")) ;

        // Toby's class (Has 1, shares none)
        db.coursesDao().insert(new Course("s4ID", "CSE 8B FA 2021 Gigantic")) ;

        //Rick's class (Has 3, shares 3)
        db.coursesDao().insert(new Course("s5ID", "CSE 20 FA 2021 Large")) ;
        db.coursesDao().insert(new Course("s5ID", "CSE 100 FA 2021 Large")) ;
        db.coursesDao().insert(new Course("s5ID", "CSE 120 FA 2021 Medium")) ;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext());
        preferences.edit().putInt("sessionId", 1).commit();
    }
  
    @Test
    // Test to make sure a students' entries shows up correctly on the search page.
    public void testViewingList(){
        try(ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(StartStopSearchActivity.class)){
            scenario.onActivity(activity -> {
                // Hack to call updateRecyclerView() to populate recycler view with values in database
                scenario.moveToState(Lifecycle.State.CREATED);
                Button stopBtn = activity.findViewById(R.id.stop_button);
                stopBtn.setVisibility(View.VISIBLE);
                scenario.moveToState(Lifecycle.State.RESUMED);
                out.println(scenario.getState());

                // Get the RecyclerView of the StudentList

                RecyclerView studentList = activity.findViewById(R.id.students_recycler_view);
                final int studentCount = studentList.getChildCount();

                // There are 4 other students in database, but only 3 should appear in the view
                assertEquals(3, studentCount);

                // Assert Mary and her information was rendered first because she shares more classes
                View studentEntry = studentList.getChildAt(1);
                TextView name = studentEntry.findViewById(R.id.classmate_name_text);
                ImageView headshot = studentEntry.findViewById(R.id.classmate_imageview);
                TextView classCount = studentEntry.findViewById(R.id.common_course_count_textview);
                ImageView wave = studentEntry.findViewById(R.id.classmate_waved);

                // Check if Mary and her information is the same as expected
                assertEquals("Mary", name.getText());
                assertEquals("mary.com", headshot.getTag());
                assertEquals("2", classCount.getText());
                assertEquals(View.VISIBLE, wave.getVisibility()); //visible hand

                // Test if the Favorite Checkbox is checked
                CheckBox favoriteIcon = studentEntry.findViewById(R.id.favorite);
                assertTrue(favoriteIcon.isChecked());

                // Log messages to visually check
                out.println("Expected: Mary        Actual: " + name.getText());
                out.println("Expected: mary.com    Actual: " + headshot.getTag());
                out.println("Expected: 2           Actual: " + classCount.getText());
                out.println("Expected: True        Actual: " + favoriteIcon.isChecked());
                out.println("Expected (show hand wave): 0 Actual: " + wave.getVisibility());

                // Assert Rick, 3 class in common, wave
                studentEntry = studentList.getChildAt(0);
                name = studentEntry.findViewById(R.id.classmate_name_text);
                headshot = studentEntry.findViewById(R.id.classmate_imageview);
                wave = studentEntry.findViewById(R.id.classmate_waved);
                classCount = studentEntry.findViewById(R.id.common_course_count_textview);
                favoriteIcon = studentEntry.findViewById(R.id.favorite);

                assertEquals("Rick", name.getText());
                assertEquals("rick.com", headshot.getTag());
                assertEquals("3", classCount.getText());
                assertFalse(favoriteIcon.isChecked());
                assertEquals(View.VISIBLE, wave.getVisibility()); //visible hand

                out.println("Expected: Rick                  Actual: " + name.getText());
                out.println("Expected: rick.com              Actual: " + headshot.getTag());
                out.println("Expected: 3                     Actual: " + classCount.getText());
                out.println("Expected: False                  Actual: " + favoriteIcon.isChecked());
                out.println("Expected (no show hand wave): 4 Actual: " + wave.getVisibility());
            });
        }
    }

    @Test
    // Test to see if Bill's hand is not waved
    public void testNoHandWaves(){
        try(ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(StartStopSearchActivity.class)){
            scenario.onActivity(activity -> {
                // Hack to call updateRecyclerView() to populate recycler view with values in database
                scenario.moveToState(Lifecycle.State.CREATED);
                Button stopBtn = activity.findViewById(R.id.stop_button);
                stopBtn.setVisibility(View.VISIBLE);
                scenario.moveToState(Lifecycle.State.RESUMED);
                out.println(scenario.getState());

                // Get the RecyclerView of the StudentList
                RecyclerView studentList = activity.findViewById(R.id.students_recycler_view);

                // Assert Bill, 1 class in common
                View studentEntry = studentList.getChildAt(2);
                TextView name = studentEntry.findViewById(R.id.classmate_name_text);
                ImageView wave = studentEntry.findViewById(R.id.classmate_waved);
                ImageView headshot = studentEntry.findViewById(R.id.classmate_imageview);
                TextView classCount = studentEntry.findViewById(R.id.common_course_count_textview);

                // Check if Bill and his information is the same as expected
                assertEquals("Bill", name.getText());
                assertEquals("bill.com", headshot.getTag());
                assertEquals("1", classCount.getText());
                assertEquals(View.INVISIBLE, wave.getVisibility()); //invisible hand

                // Check if the Favorite Checkbox is not checked
                CheckBox favoriteIcon = studentEntry.findViewById(R.id.favorite);
                favoriteIcon = studentEntry.findViewById(R.id.favorite);
                assertFalse(favoriteIcon.isChecked());

                out.println("Expected: Bill                  Actual: " + name.getText());
                out.println("Expected: bill.com    Actual: " + headshot.getTag());
                out.println("Expected: 1           Actual: " + classCount.getText());
                out.println("Expected: False        Actual: " + favoriteIcon.isChecked());
                out.println("Expected (no show hand wave): 4 Actual: " + wave.getVisibility());
            });
        }
    }
}