package com.example.birdsofafeather;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.CourseDao;
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

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void retrieveAllStudents() throws IOException {
        assertEquals(3, studentDao.count());
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

    }

    @Test
    public void compareStudents() throws IOException {

    }

    @Test
    public void getStudentCount() throws IOException {

    }

    @Test
    public void getAllCoursesForStudent() throws IOException {

    }

    @Test
    public void getSpecificCourse() throws IOException {

    }

    @Test
    public void addCourse() throws IOException {

    }

    @Test
    public void deleteCourse() throws IOException {

    }

    @Test
    public void getCourseCount() throws IOException {

    }
}
