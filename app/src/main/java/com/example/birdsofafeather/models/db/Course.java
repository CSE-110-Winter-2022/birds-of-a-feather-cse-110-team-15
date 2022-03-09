package com.example.birdsofafeather.models.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int courseId = 0;

    @ColumnInfo(name = "student_id")
    private String studentId;

    @ColumnInfo(name = "name")
    private String name;

    public Course(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    public int getCourseId() { return courseId; }

    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getStudentId() { return studentId; }

    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
