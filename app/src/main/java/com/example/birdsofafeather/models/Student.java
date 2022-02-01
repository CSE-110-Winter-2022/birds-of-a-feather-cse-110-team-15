package com.example.birdsofafeather.models;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Student {
    /** private variables
     *
     */
    private String firstName;
    private String lastName;
    private String headshot;
    private List<String> courses;

    /**
     * Default Constructor
     */
    public Student() {
        this.firstName = "";
        this.lastName = "";
        this.headshot = "";
        this.courses = new ArrayList<>();
    }

    /**
     * Constructor
     * @param firstName
     * @param lastName
     * @param headshot url
     * @param courses list of courses student has taken
     */
    public Student(String firstName, String lastName, String headshot, List<String> courses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.headshot = headshot;
        this.courses = courses;
    }

    /**
     * Setter method for student first name
     * @param newFirstName first name that will replace old one
     * @return void
     */
    public void setFirstName(String newFirstName) {
        firstName = newFirstName;
    }

    /**
     * Setter method for student last name
     * @param newLastName last name that will replace old one
     * @return void
     */
    public void setLastName(String newLastName) {
        lastName = newLastName;
    }

    /**
     * Setter method for student headshot url
     * @param newHeadshot new headshot url
     * @return void
     */
    public void setHeadshot(String newHeadshot) {
        headshot = newHeadshot;
    }

    /**
     * Setter method for setting list of courses student has taken
     * @param newCourses new list of courses to be set
     * @return void
     */
    public void setCourses(List<String> newCourses) {
        courses = newCourses;
    }

    /**
     * @return student first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return student last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return student headshot URL.
     */
    public String getHeadshot() {
       return headshot;
    }

    /**
     * @return list of student courses taken
     */
    public List<String> getCourses() {
        return courses;
    }

    /**
     * Compare if one student has taken a class with another student
     * @param otherStudent the other student being compared with
     * @return True if taken a class together, False otherwise
     */
    public boolean haveCommonCourses(Student otherStudent) {
       List<String> otherStudentCourses = otherStudent.getCourses();
       Set<String> curStudentSet = new HashSet<>(courses);

       for (String course : otherStudentCourses) {
           if (curStudentSet.contains(course))
               return true;
       }

       return false;
    }

    /**
     * @param otherStudent the other student being compared with
     * @return List of classes in common between students
     */
    public List<String> getCommonClasses(Student otherStudent) {
        List<String> otherStudentCourses = otherStudent.getCourses();
        Set<String> curStudentSet = new HashSet<>(courses);
        // store common classes
        List<String> commonClasses = new ArrayList<>();

        // go through hashset to look for common classes
        for (String course : otherStudentCourses) {
            if (curStudentSet.contains(course))
                commonClasses.add(course);
        }

        return commonClasses;
    }
}
