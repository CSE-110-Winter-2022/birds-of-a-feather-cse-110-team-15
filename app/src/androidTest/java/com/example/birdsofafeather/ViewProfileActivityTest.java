package com.example.birdsofafeather;

import static junit.framework.TestCase.assertEquals;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    Student s1;
    Student s2;
    Student s3;
    Course c1;
    Course c2;
    Course c3;

    @Before
    public void init() {
        // Initializing a database where Bob and Bill share one class, Bob and Mary share no classes
        AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
        s1 = new Student("Bob", "bob.com");
        s2 = new Student("Bill", "bill.com");
        db.studentWithCoursesDao().insert(s1);
        db.studentWithCoursesDao().insert(s2);
        c1 = new Course(1, "CSE 20 FA 2021");
        c2 = new Course(2, "CSE 20 FA 2021");
        db.coursesDao().insert(c1);
        db.coursesDao().insert(c2);
    }

    @After
    public void tearDown() {
        AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
        db.close();
    }

    @Test
    /* Tests common courses and other elements show up on profile page if they exist */
    public void testCommonCourses(){
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ViewProfileActivity.class);
        intent.putExtra("classmate_id", 3);
        try(ActivityScenario<ViewProfileActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Multiple assertions in one test to avoid launching too many activities
                TextView name = activity.findViewById(R.id.name_view);
                ImageView picture = activity.findViewById(R.id.profile_picture_view);

                // Test name is not empty
                assertEquals ("Bill", name.getText());
                // Test picture loaded
                assertEquals ("bill.com", picture.getTag());

                TextView courses = activity.findViewById(R.id.common_classes_view);
                // Test course loaded and visibility
                assertEquals(View.VISIBLE, courses.getVisibility());
                assertEquals("", courses.getText());
            });
        }
    }
}