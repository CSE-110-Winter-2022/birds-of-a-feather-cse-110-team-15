package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;
import static java.lang.System.out;

import android.util.Pair;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.Session;
import com.example.birdsofafeather.models.db.Student;
import com.example.birdsofafeather.models.db.StudentWithCourses;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SorterTest {
    Sorter sorter;
    AppDatabase db;
    StudentWithCourses me;
    Pair<StudentWithCourses, Integer> bill, mary, toby;
    List<StudentWithCourses> otherStudentsList;
    String billName = "Bill", billURL = "bill.com";
    String maryName = "Mary", maryURL = "mary.com";
    String tobyName = "Toby", tobyURL = "toby.com";
    int billCount = 1, maryCount = 2, tobyCount = 3;

    @Before
    // Initialize the database where Bob (me) is the user. And,
    // Bill shares 1 class, with recency score medium and class size score high
    // Mary shares 2, with recency score high and class size score low
    // Toby shares 3, with recency score low and class size score medium
    // Default sort:    Toby -> Mary -> Bill
    // Recency sort:    Mary -> Bill -> Toby
    // Class size sort: Bill -> Toby -> Mary
    public void init() {
        AppDatabase.useTestSingleton(ApplicationProvider.getApplicationContext());
        db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());

        // create dummy session
        db.sessionWithStudentsDao().insert(new Session("dummy"));

        String uuid = "s1ID";
        db.studentWithCoursesDao().insert(new Student(uuid, "Bob", "bob.com"));
        db.studentWithCoursesDao().insert(new Student("s2ID", billName, billURL, 1));
        db.studentWithCoursesDao().insert(new Student("s3ID", maryName, maryURL, 1));
        db.studentWithCoursesDao().insert(new Student("s4ID", tobyName, tobyURL, 1));

        // Bob's (me) classes
        db.coursesDao().insert(new Course(uuid, "CSE 21 FA 2020 Large"));
        db.coursesDao().insert(new Course(uuid, "CSE 102 FA 2020 Large"));
        db.coursesDao().insert(new Course(uuid, "CSE 103 FA 2020 Large"));
        db.coursesDao().insert(new Course(uuid, "CSE 30 WI 2021 Large"));
        db.coursesDao().insert(new Course(uuid, "MATH 20D SP 2021 Gigantic"));
        db.coursesDao().insert(new Course(uuid, "CSE 101 SS1 2021 Small"));

        // Bill's classes
        // Recency: 4, Size: 33
        db.coursesDao().insert(new Course("s2ID", "CSE 101 SS1 2021 Small"));

        // Mary's classes
        // Recency: 5, Size: 13
        db.coursesDao().insert(new Course("s3ID", "MATH 20D SP 2021 Gigantic"));
        db.coursesDao().insert(new Course("s3ID", "CSE 30 WI 2021 Large"));

        // Toby's classes
        // Recency: 3, Size: 30
        db.coursesDao().insert(new Course("s4ID", "CSE 21 FA 2020 Large"));
        db.coursesDao().insert(new Course("s4ID", "CSE 102 FA 2020 Large"));
        db.coursesDao().insert(new Course("s4ID", "CSE 103 FA 2020 Large"));

        // initialize students and otherStudentsList
        me = db.studentWithCoursesDao().get(uuid);
        bill = new Pair<>(db.studentWithCoursesDao().get("s2ID"), billCount);
        mary = new Pair<>(db.studentWithCoursesDao().get("s3ID"), maryCount);
        toby = new Pair<>(db.studentWithCoursesDao().get("s4ID"), tobyCount);

        otherStudentsList = db.sessionWithStudentsDao().get(1).getStudents();
        // initialize Sort class
        sorter = new Sorter();
    }

    // close the test database after test is done
    @After
    public void teardown() {
        db.close();
    }

    // check if both parts of students match
    public void assertStudentInfo(Pair<StudentWithCourses,Integer> expected, Pair<StudentWithCourses, Integer> actual) {
        assertEquals("Check order", expected.first.getName(), actual.first.getName());
        assertEquals("Check count", expected.second, actual.second);

    }

    // test if default sort is properly sorted by the number of common courses with the user
    // expected order is Toby -> Mary -> Bill
    @Test
    public void testDefaultSort() {
        List<Pair<StudentWithCourses, Integer>> result = sorter.sortList(Sorter.ALGORITHM.DEFAULT, me, otherStudentsList);
        // make sure order is correct
        out.println("Testing Default Sort");
        assertStudentInfo(toby, result.get(0));
        assertStudentInfo(mary, result.get(1));
        assertStudentInfo(bill, result.get(2));

        // visualize the test
        out.println("[1st] Expected: Toby    Actual: " + result.get(0).first.getName());
        out.println("[2nd] Expected: Mary    Actual: " + result.get(1).first.getName());
        out.println("[3rd] Expected: Bill    Actual: " + result.get(2).first.getName());
    }

    // test if recency sort is properly sorted by most recency to least
    // expected order is Mary -> Bill -> Toby
    @Test
    public void testRecencySort() {
        List<Pair<StudentWithCourses, Integer>> result = sorter.sortList(Sorter.ALGORITHM.RECENCY, me, otherStudentsList);
        out.println("Testing Recency Sort");

        assertStudentInfo(mary, result.get(0));
        assertStudentInfo(bill, result.get(1));
        assertStudentInfo(toby, result.get(2));

        out.println("[1st] Expected: Mary    Actual: " + result.get(0).first.getName());
        out.println("[2nd] Expected: Bill    Actual: " + result.get(1).first.getName());
        out.println("[3rd] Expected: Toby    Actual: " + result.get(2).first.getName());
    }

    // test if class size sort is properly sorted by smallest class sizes to largest
    // expected order is Bill -> Toby -> Mary
    @Test
    public void testClassSizeSort() {
        List<Pair<StudentWithCourses, Integer>> result = sorter.sortList(Sorter.ALGORITHM.CLASS_SIZE, me, otherStudentsList);
        out.println("Testing Class Size Sort");

        assertStudentInfo(bill, result.get(0));
        assertStudentInfo(toby, result.get(1));
        assertStudentInfo(mary, result.get(2));

        out.println("[1st] Expected: Bill    Actual: " + result.get(0).first.getName());
        out.println("[2nd] Expected: Toby    Actual: " + result.get(1).first.getName());
        out.println("[3rd] Expected: Mary    Actual: " + result.get(2).first.getName());
    }
}
