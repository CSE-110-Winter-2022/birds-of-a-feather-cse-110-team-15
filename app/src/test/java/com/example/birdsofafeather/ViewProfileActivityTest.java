package com.example.birdsofafeather;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.Student;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Instrumented test, which will execute on an Android device.

 */
@RunWith(AndroidJUnit4.class)
public class ViewProfileActivityTest {

    @Before
    public void init() {
        AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
        db.studentWithCoursesDao().insert(new Student("Bob", "url.com"));
        db.studentWithCoursesDao().insert(new Student("Bill", "url.com"));
        db.studentWithCoursesDao().insert(new Student("Mary", "url.com"));
        db.coursesDao().insert(new Course(1, "CSE 20 FA 2021"));
        db.coursesDao().insert(new Course(2, "CSE 20 FA 2021"));
    }

    @Test
    public void testProfileLoad(){
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ViewProfileActivity.class);
        intent.putExtra("classmate_id", 1);
        try(ActivityScenario<ViewProfileActivity> scenario = ActivityScenario.launch(intent)) {
            // Multiple assertions in one test to avoid launching too many activities
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);
                TextView name = activity.findViewById(R.id.name_view);
                ImageView picture = activity.findViewById(R.id.profile_picture_view);
                // Test name is not empty
                assert (!name.getText().equals(""));
                // Test picture loaded
                assert (picture.getTag() != null);
            });
        }
    }

    @Test
    public void testCommonCourseList(){
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ViewProfileActivity.class);
        intent.putExtra("classmate_id", 2);
        try(ActivityScenario<ViewProfileActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);
                TextView courses = activity.findViewById(R.id.common_classes_view);
                // Test course loaded and visibility
                assert (courses.getVisibility() == View.VISIBLE);
                assert (!courses.getText().equals(""));
            });
        }
    }
}