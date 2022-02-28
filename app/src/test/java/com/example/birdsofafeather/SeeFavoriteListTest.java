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
public class SeeFavoriteListTest {
    @Before
    // Initialize the database where Bob is the user, Bill shares 1 class with him, Mary shares 2,
    public void init(){
        AppDatabase.useTestSingleton(ApplicationProvider.getApplicationContext());
        AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
        db.studentWithCoursesDao().insert(new Student("Bob", "bob.com"));
        db.studentWithCoursesDao().insert(new Student("Bill", "bill.com", true));
        db.studentWithCoursesDao().insert(new Student("Mary", "mary.com"));

        // Bob's classes
        db.coursesDao().insert(new Course(1, "CSE 20 FA 2021")) ;
        db.coursesDao().insert(new Course(1, "CSE 100 FA 2021")) ;

        // Bill's class (Has 2, shares 1)
        db.coursesDao().insert(new Course(2, "CSE 20 FA 2021")) ;
        db.coursesDao().insert(new Course(2, "CSE 15L FA 2021")) ;

        // Mary's classes (Has 2, shares 2)
        db.coursesDao().insert(new Course(3, "CSE 20 FA 2021")) ;
        db.coursesDao().insert(new Course(3, "CSE 100 FA 2021")) ;
    }

    @Test
    // Test to make sure a students' entries shows up correctly on the favorite list page.
    public void testViewingFavoriteList(){
        try(ActivityScenario<FavoriteListActivity> scenario = ActivityScenario.launch(FavoriteListActivity.class)){
            scenario.onActivity(activity -> {
                RecyclerView studentList = activity.findViewById(R.id.favorites_recycler_view);
                final int studentCount = studentList.getChildCount();

                // There are 3 other students in database, only one should appear in the view
                assertEquals(1, studentCount);

                // Assert Mary and her information was rendered first because she shares more classes
                View studentEntry = studentList.getChildAt(0);
                TextView name = studentEntry.findViewById(R.id.fav_classmate_name_text);
                ImageView headshot = studentEntry.findViewById(R.id.fav_classmate_imageview);

                assertEquals("Bill", name.getText());
                assertEquals("bill.com", headshot.getTag());

                out.println("Expected: Bill        Actual: " + name.getText());
                out.println("Expected: bill.com    Actual: " + headshot.getTag());
            });
        }
    }
}

