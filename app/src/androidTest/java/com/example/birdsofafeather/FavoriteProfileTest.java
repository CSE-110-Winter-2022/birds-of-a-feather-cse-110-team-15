package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.Student;
import com.example.birdsofafeather.models.db.StudentWithCourses;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FavoriteProfileTest {

    @Before
    public void init() {
        Context context = ApplicationProvider.getApplicationContext();
        AppDatabase.useTestSingleton(context);
        AppDatabase db = AppDatabase.singleton(context);

        db.studentWithCoursesDao().insert(new Student("Bob", "bob.com"));
        db.studentWithCoursesDao().insert(new Student("Bill", "bill.com"));
        db.studentWithCoursesDao().insert(new Student("Mary", "mary.com", true));
        StudentWithCourses s1 = db.studentWithCoursesDao().get(3);

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
        intent.putExtra("classmate_id", 3);
        try (ActivityScenario<ViewProfileActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                TextView name = activity.findViewById(R.id.name_view);
                assertEquals("Mary", name.getText());
                    }
            );
        }
    }
}
