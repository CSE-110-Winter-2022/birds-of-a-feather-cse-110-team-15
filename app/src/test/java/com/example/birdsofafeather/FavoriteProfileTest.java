package com.example.birdsofafeather;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.Student;

import org.junit.Before;
import org.junit.Test;

public class FavoriteProfileTest {

    @Before
    public void init() {
        AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
        db.studentWithCoursesDao().insert(new Student("Bob", "bob.com"));
        db.studentWithCoursesDao().insert(new Student("Bill", "bill.com"));
        db.studentWithCoursesDao().insert(new Student("Mary", "mary.com", true));

        // Bob's classes
        db.coursesDao().insert(new Course(1, "CSE 20 FA 2021"));
        db.coursesDao().insert(new Course(1, "CSE 100 FA 2021"));

        // Bill's class (Has 2, shares 1)
        db.coursesDao().insert(new Course(2, "CSE 20 FA 2021"));
        db.coursesDao().insert(new Course(2, "CSE 15L FA 2021"));

        // Mary's classes (Has 2, shares 2)
        db.coursesDao().insert(new Course(3, "CSE 20 FA 2021"));
        db.coursesDao().insert(new Course(3, "CSE 100 FA 2021"));
    }

    @Test
    /* Tests common courses and other elements show up on profile page if they exist */
    public void testFavoriteFromProfile() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ViewProfileActivity.class);
        intent.putExtra("classmate_id", 2);
        try (ActivityScenario<ViewProfileActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                    }
            );
        }
    }
}
