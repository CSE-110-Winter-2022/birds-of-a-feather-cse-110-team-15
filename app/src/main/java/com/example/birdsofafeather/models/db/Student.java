package com.example.birdsofafeather.models.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class Student {
    @ColumnInfo(name = "id")
    private int studentId;

    @NonNull
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="uuid")
    private String uuid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "headshot_url")
    private String headshotURL;

    @ColumnInfo(name="favorite")
    private boolean favorite;

    @ColumnInfo(name="wavedToUser")
    private boolean wavedToUser;

    // Temporary until we can replace this version of the constructor
    @Ignore
    public Student(String name, String headshotURL) {
        this.uuid = "55555";
        this.name = name;
        this.headshotURL = headshotURL;
        this.favorite = false;        // New students begin as unfavorited
        this.wavedToUser = false;        // New students haven't waved to current user by default
    }

    public Student(@NonNull String uuid, String name, String headshotURL) {
        this.uuid = uuid;
        this.name = name;
        this.headshotURL = headshotURL;
        this.favorite = false;        // New students begin as unfavorited
        this.wavedToUser = false;        // New students haven't waved to current user by default
    }

    @Ignore
    public Student(@NonNull String uuid, String name, String headshotURL, boolean wavedToUser) {
        this(uuid, name, headshotURL);

        this.wavedToUser = wavedToUser;
    }

    @Ignore
    // Overloaded constructor for setting a favorite student (for testing)
    public Student(@NonNull String uuid, String name, String headshotURL, boolean wavedToUser, boolean favorite) {
        this(uuid, name, headshotURL, wavedToUser);

        this.favorite = favorite;
    }

    public int getStudentId() { return studentId; }

    public String getUuid() { return uuid; }

    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getName() { return name; }

    public void setWavedToUser(boolean wavedToUser) { this.wavedToUser = wavedToUser; }

    public boolean getWavedToUser() { return wavedToUser; }

    public void setName(String name) { this.name = name; }

    public String getHeadshotURL() { return headshotURL; }

    public void setHeadshotURL(String headshotURL) { this.headshotURL = headshotURL; }

    public boolean isFavorite() { return favorite; }

    public void setFavorite(boolean favorite) { this.favorite = favorite; }

}
