package com.example.birdsofafeather.models.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class Student {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int studentId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "headshot_url")
    private String headshotURL;

    @ColumnInfo(name="favorite")
    private boolean favorite;

    public Student(String name, String headshotURL) {
        this.name = name;
        this.headshotURL = headshotURL;
        this.favorite = false;        // New students begin as unfavorited
    }
    @Ignore
    // Overloaded constructor for setting a favorite student (just in case)
    public Student(String name, String headshotURL, boolean favorite) {
        this.name = name;
        this.headshotURL = headshotURL;
        this.favorite = favorite;
    }

    public int getStudentId() { return studentId; }

    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getHeadshotURL() { return headshotURL; }

    public void setHeadshotURL(String headshotURL) { this.headshotURL = headshotURL; }

    public boolean isFavorite() { return favorite; }

    public void setFavorite(boolean favorite) {this.favorite = favorite;}
}
