package com.example.birdsofafeather.models.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int courseId;

    @ColumnInfo(name = "student_id")
    public int studentId;

    @ColumnInfo(name = "name")
    public String name;

    public Course(int courseId, int studentId, String name) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.name = name;
    }
}
