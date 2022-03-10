package com.example.birdsofafeather;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.Session;
import com.example.birdsofafeather.models.db.Student;
import com.example.birdsofafeather.models.db.StudentWithCourses;

import org.junit.Before;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SorterTest {
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
        db.coursesDao().insert(new Course(uuid, "CSE 20 FA 2021")) ;
        db.coursesDao().insert(new Course(uuid, "CSE 100 FA 2021")) ;
        db.coursesDao().insert(new Course(uuid, "CSE 120 FA 2021")) ;

        // Bill's class (Has 2, shares 1)
        db.coursesDao().insert(new Course("s2ID", "CSE 20 FA 2021")) ;
        db.coursesDao().insert(new Course("s2ID", "CSE 15L FA 2021")) ;

        // Mary's classes (Has 2, shares 2)
        db.coursesDao().insert(new Course("s3ID", "CSE 20 FA 2021")) ;
        db.coursesDao().insert(new Course("s3ID", "CSE 100 FA 2021")) ;

        // Toby's class (Has 1, shares none)
        db.coursesDao().insert(new Course("s4ID", "CSE 21 FA 2020 Large")) ;
        db.coursesDao().insert(new Course("s4ID", "CSE 8B FA 2021")) ;
        db.coursesDao().insert(new Course("s4ID", "CSE 8B FA 2021")) ;
        db.coursesDao().insert(new Course("s4ID", "CSE 8B FA 2021")) ;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext());
        preferences.edit().putInt("sessionId", 1).commit();
    }
}
