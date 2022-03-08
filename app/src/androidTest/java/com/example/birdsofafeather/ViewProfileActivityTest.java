package com.example.birdsofafeather;

import static org.junit.Assert.*;
import static java.lang.System.out;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.Student;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class ViewProfileActivityTest {

    @Before
    public void init() {
        Context context = ApplicationProvider.getApplicationContext();
        String currentUserID = new UUIDManager(context).getUserUUID();
        AppDatabase.useTestSingleton(context);
        AppDatabase db = AppDatabase.singleton(context);

        db.studentWithCoursesDao().insert(new Student(currentUserID, "Bob", "bob.com"));
        db.studentWithCoursesDao().insert(new Student("s2ID", "Bill", "bill.com"));
        db.studentWithCoursesDao().insert(new Student("s3ID", "Mary", "mary.com", 0, false, true));

        // Bob's classes
        db.coursesDao().insert(new Course(currentUserID, "CSE 20 FA 2021"));
        db.coursesDao().insert(new Course(currentUserID, "CSE 100 FA 2021"));

        // Bill's class (Has 2, shares 1)
        db.coursesDao().insert(new Course("s2ID", "CSE 20 FA 2021"));
        db.coursesDao().insert(new Course("s2ID", "CSE 15L FA 2021"));

        // Mary's classes (Has 2, shares 2)
        db.coursesDao().insert(new Course("s3ID", "CSE 20 FA 2021"));
        db.coursesDao().insert(new Course("s3ID", "CSE 100 FA 2021"));
    }

    @Test
    /* Tests common courses and other elements show up on profile page if they exist */
    public void testCommonCourses(){
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ViewProfileActivity.class);
        // Pass Bill to intent
        intent.putExtra("classmate_id", "s2ID");
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
                assertEquals("CSE 20 FA 2021\n", courses.getText());
            });
        }
    }

    @Test
    /* Tests favorite checkbox and other elements show up on profile page if they exist */
    public void testFavoriteChecked() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ViewProfileActivity.class);
        // Pass Mary to intent
        intent.putExtra("classmate_id", "s3ID");
        try (ActivityScenario<ViewProfileActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Multiple assertions in one test to avoid launching too many activities
                TextView name = activity.findViewById(R.id.name_view);
                CheckBox fav = activity.findViewById(R.id.profile_favorite);

                // Test if the name is correct
                assertEquals("Mary", name.getText());

                // Test if the Favorite CheckBox is checked
                assertTrue(fav.isChecked());

                out.println("Expected: Mary    Actual:" + name.getText());
                out.println("Expected: True    Actual:" + fav.isChecked());

            });
        }
    }

    @Test
    /* Tests favorite checkbox and other elements show up on profile page if they exist */
    public void testFavoriteUnchecked() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ViewProfileActivity.class);
        // Pass Bill to intent
        intent.putExtra("classmate_id", "s2ID");
        try (ActivityScenario<ViewProfileActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Multiple assertions in one test to avoid launching too many activities
                TextView name = activity.findViewById(R.id.name_view);
                CheckBox fav = activity.findViewById(R.id.profile_favorite);

                // Test if the name is correct
                Assert.assertEquals("Bill", name.getText());

                // Test if the Favorite CheckBox is checked
                assertFalse(fav.isChecked());

                out.println("Expected: Bill    Actual:" + name.getText());
                out.println("Expected: False   Actual:" + fav.isChecked());
            });
        }
    }


}