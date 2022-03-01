package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;
import static java.lang.System.out;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.Student;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ReceiveWaveTest {
    @Before
    // Initialize the database where Bob is the user, Bill shares 1 class with him, Mary shares 2,
    // and Toby shares none.
    public void init(){
        AppDatabase.useTestSingleton(ApplicationProvider.getApplicationContext());
        AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
        String uuid = new UUIDManager(InstrumentationRegistry.getInstrumentation().getTargetContext()).getUserUUID();
        db.studentWithCoursesDao().insert(new Student(uuid,"Bob", "bob.com"));
        db.studentWithCoursesDao().insert(new Student("s2ID", "Bill", "bill.com",false));
        db.studentWithCoursesDao().insert(new Student("s3ID", "Mary", "mary.com", true));
        db.studentWithCoursesDao().insert(new Student("s4ID", "Toby", "toby.com"));
        // Bob's classes
        db.coursesDao().insert(new Course(uuid, "CSE 20 FA 2021")) ;
        db.coursesDao().insert(new Course(uuid, "CSE 100 FA 2021")) ;

        // Bill's class (Has 2, shares 1)
        db.coursesDao().insert(new Course("s2ID", "CSE 20 FA 2021")) ;
        db.coursesDao().insert(new Course("s2ID", "CSE 15L FA 2021")) ;

        // Mary's classes (Has 2, shares 2)
        db.coursesDao().insert(new Course("s3ID", "CSE 20 FA 2021")) ;
        db.coursesDao().insert(new Course("s3ID", "CSE 100 FA 2021")) ;

        // Toby's class (Has 1, shares none)
        db.coursesDao().insert(new Course("s4ID", "CSE 8B FA 2021")) ;
    }

    @Test
    // Test to make sure a students' entries shows up correctly on the search page.
    public void testViewingList(){
        try(ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(StartStopSearchActivity.class)){
            scenario.onActivity(activity -> {
                RecyclerView studentList = activity.findViewById(R.id.students_recycler_view);
                final int studentCount = studentList.getChildCount();

                // There are 3 other students in database, only 2 should appear in the view
                assertEquals(2, studentCount);

                // check if Mary Waved: should be true
                View studentEntry = studentList.getChildAt(0);
                TextView name = studentEntry.findViewById(R.id.classmate_name_text);
                ImageView headshot = studentEntry.findViewById(R.id.classmate_imageview);
                TextView classCount = studentEntry.findViewById(R.id.common_course_count_textview);
                ImageView wave = studentEntry.findViewById(R.id.classmate_waved);
                assertEquals("Mary", name.getText());
                assertEquals("mary.com", headshot.getTag());
                assertEquals("2", classCount.getText());
                assertEquals(0, wave.getVisibility()); //visible hand

                out.println("Expected: Mary        Actual: " + name.getText());
                out.println("Expected: mary.com    Actual: " + headshot.getTag());
                out.println("Expected: 2           Actual: " + classCount.getText());


                // Bill should be false, no wave bool was put
                studentEntry = studentList.getChildAt(1);
                name = studentEntry.findViewById(R.id.classmate_name_text);
                headshot = studentEntry.findViewById(R.id.classmate_imageview);
                classCount = studentEntry.findViewById(R.id.common_course_count_textview);
                wave = studentEntry.findViewById(R.id.classmate_waved);

                assertEquals("Bill", name.getText());
                assertEquals("bill.com", headshot.getTag());
                assertEquals("1", classCount.getText());
              //  assertEquals(false, wave.isChecked());

                out.println("Expected: Bill        Actual: " + name.getText());
                out.println("Expected: bill.com    Actual: " + headshot.getTag());
                out.println("Expected: 1           Actual: " + classCount.getText());
                assertEquals(4, wave.getVisibility()); //invisible hand
            });
        }
    }
}
