package com.example.birdsofafeather.models.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class Student {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int studentId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "headshot_url")
    public String headshotURL;

    public Student(int studentId, String name, String headshotURL) {
        this.studentId = studentId;
        this.name = name;
        this.headshotURL = headshotURL;
    }
}
