package com.example.birdsofafeather.models.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class Student {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int studentId = 0;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "headshot_url")
    private String headshotURL;

    public Student(String name, String headshotURL) {
        this.name = name;
        this.headshotURL = headshotURL;
    }

    public int getStudentId() { return studentId; }

    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getHeadshotURL() { return headshotURL; }

    public void setHeadshotURL(String headshotURL) { this.headshotURL = headshotURL; }
}
