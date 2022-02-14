package com.example.birdsofafeather;

import static junit.framework.TestCase.assertEquals;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
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
public class ViewProfileActivityTest {

    @Before
    public void init() {
        // Initializing a database where Bob and Bill share one class, Bob and Mary share no classes
        AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
        db.studentWithCoursesDao().insert(new Student("Bob", "bob.com"));
        db.studentWithCoursesDao().insert(new Student("Bill", "bill.com"));
        db.studentWithCoursesDao().insert(new Student("Mary", "mary.com"));
        db.coursesDao().insert(new Course(1, "CSE 20 FA 2021"));
        db.coursesDao().insert(new Course(2, "CSE 20 FA 2021"));
        db.coursesDao().insert(new Course(3, "CSE 15L FA 2021"));
    }

    @Test
    /* Tests that the name and profile profile are loaded from the database properly */
    public void testProfileLoad(){
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ViewProfileActivity.class);
        intent.putExtra("classmate_id", 2);  // Bob is looking at Bill's profile
        try(ActivityScenario<ViewProfileActivity> scenario = ActivityScenario.launch(intent)) {

            // Multiple assertions in one test to avoid launching too many activities
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);
                TextView name = activity.findViewById(R.id.name_view);
                ImageView picture = activity.findViewById(R.id.profile_picture_view);
                // Test name is not empty
                assertEquals (name.getText(), "Bill");
                // Test picture loaded
                assertEquals (picture.getTag(), "bill.com");
            });
        }
    }

    @Test
    /* Tests common courses show up on profile page if they exist */
    public void testCommonCourses(){
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ViewProfileActivity.class);
        intent.putExtra("classmate_id", 2);
        try(ActivityScenario<ViewProfileActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);
                TextView courses = activity.findViewById(R.id.common_classes_view);
                // Test course loaded and visibility
                assertEquals(courses.getVisibility(), View.VISIBLE);
                assertEquals(courses.getText(), "CSE 20 FA 2021\n");
            });
        }
    }

    @Test
    /* Tests no common courses show up on profile page when they don't exist*/
    public void testNoCommonCourses(){
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ViewProfileActivity.class);
        intent.putExtra("classmate_id", 3); // Bob is looking at Mary's profile
        try(ActivityScenario<ViewProfileActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);
                TextView courses = activity.findViewById(R.id.common_classes_view);
                assertEquals(courses.getVisibility(), View.VISIBLE);
                assertEquals(courses.getText(), "\n");
            });
        }
    }
}