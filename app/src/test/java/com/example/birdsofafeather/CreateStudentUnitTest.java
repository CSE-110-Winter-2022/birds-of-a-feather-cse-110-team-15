package com.example.birdsofafeather;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.birdsofafeather.models.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateStudentUnitTest {
    private Student student1;
    private Student student2;
    private Student student3;
    private Student student4;

    private List<String> student2Classes;
    private List<String> student3Classes;
    private List<String> student4Classes;

    @Before
    public void initiate() {
        student1 = new Student();

        student2Classes = new ArrayList<>(Arrays.asList(new String[] {"CSE 30 Fall 2020", "CSE 100 Winter 2021", "MATH 20C Fall 2019"}));
        student2 = new Student("Joe", "Burrow", "www.photo.com", student2Classes);

        student3Classes = new ArrayList<>(Arrays.asList(new String[] {"CSE 11 Fall 2020", "CSE 30 Winter 2021", "MATH 20A Fall 2019"}));
        student3 = new Student("Ashley", "Roe", "www.photo2.com", student3Classes);

        student4Classes = new ArrayList<>(Arrays.asList(new String[] {"CSE 30 Fall 2020", "CSE 100 Winter 2021", "MATH 20A Fall 2019"}));
        student4 = new Student("John", "Doe", "www.photo2.com", student4Classes);
    }

    @Test
    public void createDefaultStudentCorrect() {
        // default constructor
        assertEquals("default first name incorrect", student1.getFirstName(), "");
        assertEquals("default last name incorrect", student1.getLastName(), "");
        assertEquals("default headshot url incorrect", student1.getHeadshot(), "");
        assertEquals("default course list incorrect", student1.getCourses().size(), 0);
    }

    @Test
    public void createStudentWithValuesCorrect() {
        // constructor with parameters
        assertEquals("first name incorrect", student2.getFirstName(), "Joe");
        assertEquals("last name incorrect", student2.getLastName(), "Burrow");
        assertEquals("headshot url incorrect", student2.getHeadshot(), "www.photo.com");
        for (int i = 0; i < student2Classes.size(); i++) {
           assertEquals("Course list initialized incorrectly", student2.getCourses().get(i), student2Classes.get(i));
        }
    }

    @Test
    public void modifyStudentValues() {
        // set first name
        String newFirstName = "Billy";
        student2.setFirstName(newFirstName);
        assertEquals("Set first name incorrectly", student2.getFirstName(), newFirstName);

        // set last name
        String newLastName = "Jennings";
        student2.setLastName(newLastName);
        assertEquals("Set last name incorrectly", student2.getLastName(), newLastName);

        // set headshot url
        String newHeadshot = "www.newphoto.com";
        student2.setHeadshot(newHeadshot);
        assertEquals("Set headshot url incorrectly", student2.getHeadshot(), newHeadshot);

        // set list of classes taken
        student2.setCourses(student3Classes);
        for (int i = 0; i < student3Classes.size(); i++) {
            assertEquals("set list of classes incorrectly", student2.getCourses().get(i), student3Classes.get(i));
        }
    }

    @Test
    public void editCoursesTaken() {
       // add duplicate
       String oldCourseTaken = "CSE 30 Fall 2020";
       assertFalse(student2.addCourse(oldCourseTaken));

       // add new course
       String newCourseTaken = "CSE 101 Spring 2021";
       assertTrue(student2.addCourse(newCourseTaken));
       assertTrue(student2.getCourses().contains(newCourseTaken));

       // remove class that doesn't exist in list
       String newCourse = "CSE 131 Spring 2020";
       assertFalse(student2.removeCourse(newCourse));
       assertFalse(student2.getCourses().contains(newCourse));

       // remove class on empty list
       assertFalse(student1.removeCourse(newCourse));
       assertEquals(student1.getCourses().size(), 0);

       // remove class that exists
       assertTrue(student2.removeCourse(oldCourseTaken));
       assertFalse(student2.getCourses().contains(oldCourseTaken));
    }

    @Test
    public void testUniqueStudents() {
        List<String> commonCourses = student2.getCommonCourses(student3);
        assertEquals("Supposed to be empty", commonCourses.size(), 0);
    }

    @Test
    public void testCommonStudents() {
        List<String> commonCourses = student2.getCommonCourses(student4);
        List<String> correctRes = new ArrayList<>(Arrays.asList(new String[] {"CSE 30 Fall 2020", "CSE 100 Winter 2021"}));
        for (int i = 0; i < correctRes.size(); i++) {
            assertEquals("wrong classes", commonCourses.get(i), correctRes.get(i));
        }
    }
}
