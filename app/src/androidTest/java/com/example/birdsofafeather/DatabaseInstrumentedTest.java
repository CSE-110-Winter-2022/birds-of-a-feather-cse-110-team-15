package com.example.birdsofafeather;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.CourseDao;
import com.example.birdsofafeather.models.db.Student;
import com.example.birdsofafeather.models.db.StudentWithCourses;
import com.example.birdsofafeather.models.db.StudentWithCoursesDao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DatabaseInstrumentedTest {
    private StudentWithCoursesDao studentDao;
    private CourseDao courseDao;
    private AppDatabase db;

    @Before
    public void init() {
        Context context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.singleton(context, "test1-students.db");
        studentDao = db.studentWithCoursesDao();
        courseDao = db.coursesDao();
    }

    @Test
    public void retrieveAllStudents() {
        // check for correct count
        assertEquals(4, studentDao.count());
        // check if all students retrieved
        List<StudentWithCourses> students = studentDao.getAll();
        assertEquals(students.get(0).getId(), 1);
        assertEquals(students.get(1).getId(), 2);
        assertEquals(students.get(2).getId(), 3);
        assertEquals(students.get(3).getId(), 4);
    }

    @Test
    public void retrieveStudentObject() {
        // retrieve student that exists in database
        StudentWithCourses student = studentDao.get(2);
        assertEquals(2, student.getId());
        assertEquals("Bill", student.getName());
        assertEquals("https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0", student.getHeadshotURL());
        assertEquals(Arrays.asList("MATH 180A FA 2020", "CSE 100 SP 2021", "CSE 101 FA 2021"), student.getCourses());

        // get student that doesn't exist
        assertNull(studentDao.get(111));
    }

    @Test
    public void addStudentObject() {
        // add a new student
        Student student = new Student(studentDao.count()+1, "Josh Doe", "link.com");
        studentDao.add(student);
        StudentWithCourses retrievedStudent = studentDao.get(studentDao.count());
        assertEquals(5, retrievedStudent.getId());
        assertEquals("Josh Doe", retrievedStudent.getName());
        assertEquals("link.com", retrievedStudent.getHeadshotURL());
        assertEquals(0, retrievedStudent.getCourses().size());

        // test delete student not in database
        studentDao.delete(new Student(21, "asdf", "asdf"));

        // revert back to original state / test delete
        studentDao.delete(student);
        assertNull(studentDao.get(5));
        assertEquals(studentDao.count(), 4);
    }

    @Test
    public void compareStudents() {
        // test for NO common classes
        StudentWithCourses s1 = studentDao.get(1);
        StudentWithCourses s2 = studentDao.get(2);
        StudentWithCourses s3 = studentDao.get(3);
        assertEquals(0, s2.getCommonCourses(s3).size());

        // test for common classes
        assertEquals(Arrays.asList("CSE 100 SP 2021"), s1.getCommonCourses(s2));

        // test comparing with a student not in database
        StudentWithCourses s4 = studentDao.get(10);
        assertEquals(0, s1.getCommonCourses(s4).size());
    }

    @Test
    public void getAllCoursesForStudent() {
        // retrieve for student with courses inputted already
        assertEquals(courseDao.getForStudent(1).size(), 4);

        // retrieve for student with no courses inputted yet
        assertEquals(courseDao.getForStudent(4).size(), 0);

        // retrieve for student that doesn't exist
        assertEquals(courseDao.getForStudent(10).size(), 0);
    }

    @Test
    public void getSpecificCourse() {
        // get course that exists
        assertEquals(courseDao.get(1).name, "CSE 21 FA 2020");
        // get course that doesn't exist
        assertNull(courseDao.get(20));
    }

    @Test
    public void addDeleteCourse() {
        // add a new course
        Course newCourse = new Course(courseDao.count()+1, 3, "CSE 200 FA 2021");
        courseDao.insert(newCourse);
        assertEquals(courseDao.get(courseDao.count()).name, "CSE 200 FA 2021");
        assertTrue(studentDao.get(3).getCourses().contains("CSE 200 FA 2021"));

        // delete course that doesn't exist
        courseDao.delete(new Course(111, 212, "asdf"));

        // revert back to original state / test delete
        courseDao.delete(newCourse);
        assertNull(courseDao.get(10));
    }

    @Test
    public void getCourseCount() {
        assertEquals(courseDao.count(), 9);
    }
}
