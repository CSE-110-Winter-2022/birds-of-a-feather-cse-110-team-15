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
import com.example.birdsofafeather.models.db.Session;
import com.example.birdsofafeather.models.db.SessionWithStudents;
import com.example.birdsofafeather.models.db.SessionWithStudentsDao;
import com.example.birdsofafeather.models.db.Student;
import com.example.birdsofafeather.models.db.StudentWithCourses;
import com.example.birdsofafeather.models.db.StudentWithCoursesDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private StudentWithCoursesDao studentDao;
    private CourseDao courseDao;
    private SessionWithStudentsDao sessionDao;
    private AppDatabase db;
    private Session session1;
    private Session session2;
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
        sessionDao = db.sessionWithStudentsDao();
        session1 = new Session("CSE 30");
        session2 = new Session("02/25/2022 19:30");
        s1 = new Student("John", "link.com");
        s2 = new Student("Mary", "link.com", 1);
        s3 = new Student("Nancy", "link.com", 2);
        c1 = new Course(1, "CSE 30 FA 2021");
        c2 = new Course(1, "CSE 101 WI 2021");
        c3 = new Course(1, "CSE 21 SP 2021");
        c4 = new Course(2, "CSE 30 FA 2021");
        c5 = new Course(2, "CSE 101 WI 2021");
        c6 = new Course(2, "CSE 105 FA 2021");
        c7 = new Course(3, "CSE 11 FA 2020");
        c8 = new Course(3, "CSE 110 WI 2021");
    }

    @After
    public void teardown() {
        db.close();
    }

    @Test
    public void addDeleteStudentObject() {
        // add a new student
        studentDao.insert(s1);
        // have to set studentId after generated, so kind of extra step
        s1.setStudentId(studentDao.count());
        StudentWithCourses retrievedStudent = studentDao.get(s1.getStudentId());

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
        assertNull(studentDao.get(s1.getStudentId()));
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
        assertEquals(students.get(0).getStudentId(), 1);
        assertEquals(students.get(1).getStudentId(), 2);
        assertEquals(students.get(2).getStudentId(), 3);
    }

    @Test
    public void retrieveStudentObject() {
        // retrieve student that exists in database
        studentDao.insert(s1);
        courseDao.insert(c1);
        courseDao.insert(c2);
        StudentWithCourses student = studentDao.get(1);
        assertEquals(1, student.getStudentId());
        assertEquals("John", student.getName());
        assertEquals("link.com", student.getHeadshotURL());
        assertEquals(Arrays.asList("CSE 30 FA 2021", "CSE 101 WI 2021"), student.getCourses());

        // get student that doesn't exist
        assertNull(studentDao.get(111));
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

        StudentWithCourses st1 = studentDao.get(1);
        StudentWithCourses st2 = studentDao.get(2);
        StudentWithCourses st3 = studentDao.get(3);
        // test for NO common classes
        assertEquals(0, st1.getCommonCourses(st3).size());

        // test for common classes
        assertEquals(Arrays.asList("CSE 30 FA 2021", "CSE 101 WI 2021"), st1.getCommonCourses(st2));

        // test comparing with a student not in database
        StudentWithCourses s4 = studentDao.get(10);
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
        assertEquals(courseDao.getForStudent(1).size(), 2);

        // retrieve for student with no courses inputted yet
        assertEquals(courseDao.getForStudent(2).size(), 0);

        // retrieve for student that doesn't exist
        assertEquals(courseDao.getForStudent(10).size(), 0);
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
        assertTrue(studentDao.get(1).getCourses().contains("CSE 30 FA 2021"));

        // delete course that doesn't exist
        courseDao.delete(new Course(212, "idk"));

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

    @Test
    public void insertDeleteSession() {
        // test insert session
        int id = (int) sessionDao.insert(session1);
        assertEquals(1, id);
        System.out.println("Expect: CSE 30\nActual: " + sessionDao.get(1).getName());
        assertEquals("Session Name", "CSE 30", sessionDao.get(1).getName());
        // delete session
        sessionDao.delete(sessionDao.get(1).getSession());
        assertEquals("Session Count after deletion", 0, sessionDao.count());
    }

    @Test
    public void getAllSessions() {
        // insert into database
        sessionDao.insert(session1);
        sessionDao.insert(session2);
        List<SessionWithStudents> sessionsList = sessionDao.getAll();
        assertEquals("First session in database", "CSE 30", sessionsList.get(0).getName());
        assertEquals("Second session in database", "02/25/2022 19:30", sessionsList.get(1).getName());
    }

    @Test
    public void getStudentsFromSession() {
        // set up database
        sessionDao.insert(session1);
        sessionDao.insert(session2);
        studentDao.insert(s2);
        courseDao.insert(c4);
        courseDao.insert(c5);
        courseDao.insert(c6);
        studentDao.insert(new Student("John", "link.com", 1));
        studentDao.insert(s3);
        SessionWithStudents curSession = sessionDao.get(1);
        assertEquals("Session count", 2, curSession.getStudents().size());
        // check if can retrieve student's courses
        for (String course : curSession.getStudents().get(1).getCourses()) {
            System.out.println(course);
        }

    }

    @Test
    public void changeSessionName() {
        sessionDao.insert(session2);
        SessionWithStudents retrievedSession = sessionDao.get(1);
        retrievedSession.setName("CSE 105");
        // update database
        sessionDao.updateSession(retrievedSession.getSession());

        // check if updated
        SessionWithStudents retrieved = sessionDao.get(1);
        assertEquals("Get session name", "CSE 105", retrieved.getName());
    }
}
