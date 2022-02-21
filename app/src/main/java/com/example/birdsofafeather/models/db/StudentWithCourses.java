package com.example.birdsofafeather.models.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

public class StudentWithCourses {
    @Embedded
    public Student student;

    @Relation(parentColumn = "id", entityColumn = "student_id", entity = Course.class, projection = {"name"})
    public List<String> courses;

    public int getStudentId() { return student.getStudentId(); }

    public void setStudentId(int studentId) { student.setStudentId(studentId); }

    public String getName() { return student.getName(); }

    public void setName(String name) { student.setName(name); }

    public String getHeadshotURL() { return student.getHeadshotURL(); }

    public void setHeadshotURL(String headshotURL) { student.setHeadshotURL(headshotURL); }

    public boolean isFavorite() { return student.isFavorite(); }

    public void setFavorite(boolean favorite) {student.setFavorite(favorite); }

    public List<String> getCourses() { return courses; }

    public List<String> getCommonCourses(StudentWithCourses otherStudent) {
        List<String> commonCourses = new ArrayList<>(courses);
        if (otherStudent == null) return new ArrayList<>();
        commonCourses.retainAll(otherStudent.getCourses());
        return commonCourses;
    }
}
