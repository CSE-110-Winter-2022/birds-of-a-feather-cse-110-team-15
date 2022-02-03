package com.example.birdsofafeather.models.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.birdsofafeather.models.IStudent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudentWithCourses implements IStudent {
    @Embedded
    public Student student;

    @Relation(parentColumn = "id", entityColumn = "student_id", entity = Course.class, projection = {"name"})
    public List<String> courses;

    @Override
    public int getId() {
        return this.student.studentId;
    }

    @Override
    public String getName() {
        return this.student.name;
    }

    @Override
    public String getHeadshotURL() {
        return this.student.headshotURL;
    }

    @Override
    public List<String> getCourses() {
        return this.courses;
    }

    public List<String> getCommonCourses(StudentWithCourses otherStudent) {
        Set<String> curStudentCourseSet = new HashSet<>(this.courses);
        List<String> commonCourses = new ArrayList<>();
        for (String course : otherStudent.courses) {
            if (curStudentCourseSet.contains(course))
                commonCourses.add(course);
        }
        return commonCourses;
    }
}
