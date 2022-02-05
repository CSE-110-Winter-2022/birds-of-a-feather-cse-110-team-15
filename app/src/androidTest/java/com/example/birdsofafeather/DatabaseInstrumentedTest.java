package com.example.birdsofafeather;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.birdsofafeather.models.IStudent;
import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.CourseDao;
import com.example.birdsofafeather.models.db.Student;
import com.example.birdsofafeather.models.db.StudentWithCourses;
import com.example.birdsofafeather.models.db.StudentWithCoursesDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.*;

import java.io.IOException;
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
        db = AppDatabase.singleton(context);
        studentDao = db.studentWithCoursesDao();
        courseDao = db.coursesDao();
    }

//    @After
//    public void closeDb() throws IOException {
//        db.close();
//    }

    @Test
    public void retrieveAllStudents() throws IOException {
        // check for correct count
        assertEquals(3, studentDao.count());
        // check if all students retrieved
        List<StudentWithCourses> students = studentDao.getAll();
        assertEquals(students.get(0).getId(), 1);
        assertEquals(students.get(1).getId(), 2);
        assertEquals(students.get(2).getId(), 3);
    }

    @Test
    public void retrieveStudentObject() throws IOException {
        StudentWithCourses student = studentDao.get(2);
        assertEquals(2, student.getId());
        assertEquals("Bill", student.getName());
        assertEquals("https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0", student.getHeadshotURL());
        assertEquals(Arrays.asList("MATH 180A FA 2020", "CSE 100 SP 2021", "CSE 101 FA 2021"), student.getCourses());
    }

    @Test
    public void addStudentObject() throws IOException {
        Student student = new Student(studentDao.count()+1, "Josh Doe", "link.com");
        studentDao.add(student);
        IStudent retrievedStudent = studentDao.get(studentDao.count());
        assertEquals(4, retrievedStudent.getId());
        assertEquals("Josh Doe", retrievedStudent.getName());
        assertEquals("link.com", retrievedStudent.getHeadshotURL());
        assertEquals(0, retrievedStudent.getCourses().size());

        // revert back to original state / test delete
        studentDao.delete(student);
        assertNull(studentDao.get(4));
        assertEquals(studentDao.count(), 3);
    }

    @Test
    public void compareStudents() throws IOException {
        // test for NO common classes
        StudentWithCourses s1 = studentDao.get(1);
        StudentWithCourses s2 = studentDao.get(2);
        StudentWithCourses s3 = studentDao.get(3);
        assertEquals(s2.getCommonCourses(s3).size(), 0);

        // test for common classes
        assertEquals(s1.getCommonCourses(s2), Arrays.asList("CSE 100 SP 2021"));
    }

    @Test
    public void getAllCoursesForStudent() throws IOException {

        // retrieve for student with courses inputted already
        assertEquals(courseDao.getForStudent(1).size(), 4);

        // retrieve for student with no courses inputted yet AND/OR doesn't exist
        Student newStudent = new Student(studentDao.count()+1, "testing", "link.com");
        studentDao.add(newStudent);
        assertEquals(courseDao.getForStudent(studentDao.count()).size(), 0);
        assertEquals(courseDao.getForStudent(10).size(), 0);
        // revert to original state
        studentDao.delete(newStudent);
    }

    @Test
    public void getSpecificCourse() throws IOException {
        // get course that exists
        assertEquals(courseDao.get(1).name, "CSE 21 FA 2020");
        // get course that doesn't exist
        assertNull(courseDao.get(20));
    }

    @Test
    public void addDeleteCourse() throws IOException {
        Course newCourse = new Course(courseDao.count()+1, 3, "CSE 200 FA 2021");
        courseDao.insert(newCourse);
        assertEquals(courseDao.get(courseDao.count()).name, "CSE 200 FA 2021");
        assertTrue(studentDao.get(3).getCourses().contains("CSE 200 FA 2021"));

        // revert back to original state / test delete
        courseDao.delete(newCourse);
        assertNull(courseDao.get(10));
    }

    @Test
    public void getCourseCount() throws IOException {
        assertEquals(courseDao.count(), 9);
    }
}
