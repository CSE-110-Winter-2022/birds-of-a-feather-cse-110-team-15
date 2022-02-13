package com.example.birdsofafeather;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.CourseDao;
import com.example.birdsofafeather.models.db.Student;
import com.example.birdsofafeather.models.db.StudentWithCourses;
import com.example.birdsofafeather.models.db.StudentWithCoursesDao;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;


/**
 * Instrumented test, which will execute on an Android device.

 */
@RunWith(AndroidJUnit4.class)
public class ViewProfileActivityTest {

    private StudentWithCoursesDao studentDao;
    private CourseDao courseDao;
    private AppDatabase db;
    private Student s1;
    private Student s2;
    private Student s3;
    private Course c1;
    private Course c2;
    private Course c3;
    private Course c4;
    private Course c5;
    private Course c6;
    private Course c7;
    private Course c8;

    @Before
    public void init() {
        Context context = getApplicationContext();
        AppDatabase.useTestSingleton(context);
        db = AppDatabase.singleton(context);
        studentDao = db.studentWithCoursesDao();
        courseDao = db.coursesDao();
        s1 = new Student("John", "link.com");
        s2 = new Student("Mary", "link.com");
        s3 = new Student("Nancy", "link.com");
        c1 = new Course(1, "CSE 30 FA 2021");
        c2 = new Course(1, "CSE 101 WI 2021");
        c3 = new Course(1, "CSE 21 SP 2021");
        c4 = new Course(2, "CSE 30 FA 2021");
        c5 = new Course(2, "CSE 101 WI 2021");
        c6 = new Course(2, "CSE 105 FA 2021");
        c7 = new Course(3, "CSE 101 WI 2021");
        c8 = new Course(3, "CSE 110 WI 2021");
    }

    @Rule
    public ActivityScenarioRule<ViewProfileActivity> rule =
            new ActivityScenarioRule<>(ViewProfileActivity.class);

    @Test
    public void testProfileLoad(){
        // Multiple assertions in one test to avoid launching too many activities
        ActivityScenario<ViewProfileActivity> scenario = rule.getScenario();
        scenario.onActivity(activity -> {
            TextView name = activity.findViewById(R.id.name_view);
            ImageView picture = activity.findViewById(R.id.profile_picture_view);
            // Test name is not empty
            assert(!name.getText().equals(""));
            // Test picture loaded
            assert(picture.getTag() != null);
        });
    }

    @Test
    public void testCommonCourseList(){
        ActivityScenario<ViewProfileActivity> scenario = rule.getScenario();
        scenario.onActivity(activity -> {
            TextView courses = activity.findViewById(R.id.common_classes_view);
            // Test course loaded and visibility
            assert(courses.getVisibility() == View.VISIBLE);
            assert(!courses.getText().equals(""));
        });
    }

    @Test
    public void testCommonCourse_1(){
        ActivityScenario<ViewProfileActivity> scenario = rule.getScenario();
        AppDatabase db = AppDatabase.singleton(getApplicationContext());

        //insert the first student
        studentDao.insert(s1);
        //set the id of the first student
        s1.setStudentId(studentDao.count());
        //add course for the first student
        courseDao.insert(c1);
        courseDao.insert(c2);
        courseDao.insert(c3);

        //insert the second student
        studentDao.insert(s2);
        //set the id of the second student
        s2.setStudentId(studentDao.count());
        //add course for the second student
        courseDao.insert(c4);
        courseDao.insert(c5);
        courseDao.insert(c6);

        //insert the third student
        studentDao.insert(s3);
        //set the id of the third student
        s3.setStudentId(studentDao.count());
        //add course for the second student
        courseDao.insert(c7);
        courseDao.insert(c8);

        //init students to test
        StudentWithCourses test_1 = db.studentWithCoursesDao().get(s1.getStudentId());
        StudentWithCourses test_2 = db.studentWithCoursesDao().get(s2.getStudentId());
        StudentWithCourses test_3 = db.studentWithCoursesDao().get(s3.getStudentId());

        //get common course list 1
        List<String> cc_test_1 = test_1.getCommonCourses(test_2);

        //get common course list 2
        List<String> cc_test_2 = test_2.getCommonCourses(test_3);

        //get common course list 3
        List<String> cc_test_3 = test_1.getCommonCourses(test_3);

        //test expected and actual common course output
        assertEquals("CSE 101 WI 2021", cc_test_1);
        assertEquals("CSE 101 WI 2021", cc_test_2);
        assertEquals("CSE 101 WI 2021", cc_test_3);
    }
}