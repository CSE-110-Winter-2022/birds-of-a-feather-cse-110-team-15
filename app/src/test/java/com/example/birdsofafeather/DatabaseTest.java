package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.CourseDao;
import com.example.birdsofafeather.models.db.Student;
import com.example.birdsofafeather.models.db.StudentWithCourses;
import com.example.birdsofafeather.models.db.StudentWithCoursesDao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
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
        Context context = ApplicationProvider.getApplicationContext();
        AppDatabase.useTestSingleton(context);
        db = AppDatabase.singleton(context);
        studentDao = db.studentWithCoursesDao();
        courseDao = db.coursesDao();
        s1 = new Student("s1ID", "John", "link.com");
        s2 = new Student("s2ID", "Mary", "link.com");
        s3 = new Student("s3ID" , "Nancy", "link.com");
        c1 = new Course("s1ID", "CSE 30 FA 2021");
        c2 = new Course("s1ID", "CSE 101 WI 2021");
        c3 = new Course("s1ID", "CSE 21 SP 2021");
        c4 = new Course("s2ID", "CSE 30 FA 2021");
        c5 = new Course("s2ID", "CSE 101 WI 2021");
        c6 = new Course("s2ID", "CSE 105 FA 2021");
        c7 = new Course("s3ID", "CSE 11 FA 2020");
        c8 = new Course("s3ID", "CSE 110 WI 2021");
    }

    @Test
    public void addDeleteStudentObject() {
        // add a new student
        studentDao.insert(s1);
        // have to set studentId after generated, so kind of extra step
        s1.setStudentId(studentDao.count());
        StudentWithCourses retrievedStudent = studentDao.get(s1.getUuid());

        assertEquals(1, retrievedStudent.getStudentId());
        assertEquals("John", retrievedStudent.getName());
        assertEquals("link.com", retrievedStudent.getHeadshotURL());
        assertEquals(0, retrievedStudent.getCourses().size());

        // test delete student not in database
        studentDao.delete(s2);

        studentDao.insert(s2);
        // delete student that exists
        studentDao.delete(s1);

        // make sure id is being updated correctly
        assertNull(studentDao.get(s1.getUuid()));
        assertEquals(2, studentDao.lastIdCreated());
        studentDao.insert(s3);
        assertEquals(3, studentDao.lastIdCreated());
    }


    @Test
    public void retrieveAllStudents() {
        // test retrieving empty database
        assertEquals(0, studentDao.getAll().size());

        studentDao.insert(s1);
        studentDao.insert(s2);
        studentDao.insert(s3);

        // check if all students retrieved
        List<StudentWithCourses> students = studentDao.getAll();
        assert(students.get(0).getUUID().equals("s1ID"));
        assert(students.get(1).getUUID().equals("s2ID"));
        assert(students.get(2).getUUID().equals("s3ID"));
    }

    @Test
    public void retrieveStudentObject() {
        // retrieve student that exists in database
        studentDao.insert(s1);
        courseDao.insert(c1);
        courseDao.insert(c2);

        StudentWithCourses student = studentDao.get("s1ID");

        assertEquals("s1ID", student.getUUID());
        assertEquals("John", student.getName());
        assertEquals("link.com", student.getHeadshotURL());
        assertEquals(Arrays.asList("CSE 30 FA 2021", "CSE 101 WI 2021"), student.getCourses());

        // get student that doesn't exist
        assertNull(studentDao.get("nonId"));
    }

    @Test
    public void compareStudents() {
        // set up scenario
        studentDao.insert(s1);
        studentDao.insert(s2);
        studentDao.insert(s3);
        courseDao.insert(c1);
        courseDao.insert(c2);
        courseDao.insert(c3);
        courseDao.insert(c4);
        courseDao.insert(c5);
        courseDao.insert(c6);
        courseDao.insert(c7);
        courseDao.insert(c8);

        StudentWithCourses st1 = studentDao.get("s1ID");
        StudentWithCourses st2 = studentDao.get("s2ID");
        StudentWithCourses st3 = studentDao.get("s3ID");
        // test for NO common classes
        assertEquals(0, st1.getCommonCourses(st3).size());

        // test for common classes
        assertEquals(Arrays.asList("CSE 30 FA 2021", "CSE 101 WI 2021"), st1.getCommonCourses(st2));

        // test comparing with a student not in database
        StudentWithCourses s4 = studentDao.get("notID");
        assertEquals(0, st1.getCommonCourses(s4).size());
    }

    @Test
    public void getAllCoursesForStudent() {
        // set up scenario
        studentDao.insert(s1);
        studentDao.insert(s2);
        courseDao.insert(c1);
        courseDao.insert(c2);

        // retrieve for student with courses inputted already
        assertEquals(courseDao.getForStudent("s1ID").size(), 2);

        // retrieve for student with no courses inputted yet
        assertEquals(courseDao.getForStudent("s2ID").size(), 0);

        // retrieve for student that doesn't exist
        assertEquals(courseDao.getForStudent("notID").size(), 0);
    }

    @Test
    public void getSpecificCourse() {
        // set up scenario
        courseDao.insert(c1);
        courseDao.insert(c2);

        // get course that exists
        assertEquals(courseDao.get(1).getName(), "CSE 30 FA 2021");
        // get course that doesn't exist
        assertNull(courseDao.get(20));
    }

    @Test
    public void addDeleteCourse() {
        // add a new course
        studentDao.insert(s1);
        courseDao.insert(c1);
        assertEquals(courseDao.get(courseDao.count()).getName(), "CSE 30 FA 2021");
        assertTrue(studentDao.get("s1ID").getCourses().contains("CSE 30 FA 2021"));

        // delete course that doesn't exist
        courseDao.delete(new Course("idk", "idk"));

        // check id generation is correct
        courseDao.insert(c2);
        courseDao.delete(c1);
        assertEquals(2, courseDao.lastIdCreated());
        courseDao.insert(c3);
        assertEquals(3, courseDao.lastIdCreated());
        assertEquals(3, courseDao.get(3).getCourseId());
    }

    @Test
    public void getCourseCount() {
        // test empty database
        assertEquals(courseDao.count(), 0);

        // test populated database
        courseDao.insert(c1);
        courseDao.insert(c2);
        assertEquals(courseDao.count(), 2);
    }
}
