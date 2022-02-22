package com.example.birdsofafeather;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
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
//
//    @After
//    public void cleanup() {
//        AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
//        db.clearAllTables();
//    }
    @Test
    // Test to make sure the favorite icon works from the student search list
    public void testFavoriteStudentsFromList(){
        try(ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(StartStopSearchActivity.class)){
            scenario.onActivity(activity -> {
                AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
                // Mary is the student we will be favoriting/unfavoriting. She has ID 3 in our db

                RecyclerView studentList = activity.findViewById(R.id.students_recycler_view);
                // Getting a the first entry in the RecyclerView, which should be Mary
                View studentEntry = studentList.getChildAt(0);
                // Get favorite icon (actually a checkbox) at that specific entry
                CheckBox favoriteIcon = studentEntry.findViewById(R.id.favorite);

                StudentWithCourses mary = db.studentWithCoursesDao().get(3);
                assertFalse(mary.isFavorite());
                favoriteIcon.setChecked(true);          // We've favorited Mary.

                // Reload Mary from database and check she's favorited
                mary = db.studentWithCoursesDao().get(3);
                assertTrue(mary.isFavorite());

                // Unfavorite Mary again
                favoriteIcon.setChecked(false);
                mary = db.studentWithCoursesDao().get(3);
                assertFalse(mary.isFavorite());
            });
        }
    }

    @Test
    // Test to make sure favorite icon works from the profile page (using same database)
    public void testFavoriteStudentsFromProfile(){
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ViewProfileActivity.class);
        intent.putExtra("classmate_id", 3);
        try(ActivityScenario<ViewProfileActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
                TextView name = activity.findViewById(R.id.name_view);
                CheckBox favoriteIcon = activity.findViewById(R.id.profile_favorite);

                assertEquals("Mary", name.getText());

                // Same steps as previous test
                StudentWithCourses mary = db.studentWithCoursesDao().get(3);

                assertFalse(mary.isFavorite());
                favoriteIcon.setChecked(true);

                mary = db.studentWithCoursesDao().get(3);
                assertTrue(mary.isFavorite());
            });
        }
    }
}
