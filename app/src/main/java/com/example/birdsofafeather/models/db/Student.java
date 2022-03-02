package com.example.birdsofafeather.models.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "students")
public class Student {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    private int id;

    @NonNull
    @ColumnInfo(name="uuid")
    private String uuid;

    @ColumnInfo(name = "session_id")
    private int sessionId = 0;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "headshot_url")
    private String headshotURL;

    @ColumnInfo(name="favorite")
    private boolean favorite = false;

    @ColumnInfo(name="wavedToUser")
    private boolean wavedToUser;

    @ColumnInfo(name="wavedFromUser")
    private boolean wavedFromUser;

    public Student(@NonNull String uuid, String name, String headshotURL, int sessionId) {
        this.uuid = uuid;
        this.name = name;
        this.headshotURL = headshotURL;
        this.sessionId = sessionId;
        this.favorite = false;        // New students begin as unfavorited
        this.wavedToUser = false;        // New students haven't waved to current user by default
    }

    // Without sessions, for testing
    @Ignore
    public Student(@NonNull String uuid, String name, String headshotURL) {
        this.uuid = uuid;
        this.name = name;
        this.headshotURL = headshotURL;
        this.favorite = false;        // New students begin as unfavorited
        this.wavedToUser = false;        // New students haven't waved to current user by default
    }


    // Overloaded constructor for setting a student with no sessionId (for testing)
    @Ignore
    public Student(@NonNull String uuid, String name, String headshotURL, int sessionId, boolean wavedToUser) {
        this(uuid, name, headshotURL, sessionId);

        this.wavedToUser = wavedToUser;
    }

    @Ignore
    // Overloaded constructor for setting a favorite student (for testing)
    public Student(@NonNull String uuid, String name, String headshotURL, int sessionId, boolean wavedToUser, boolean favorite) {
        this(uuid, name, headshotURL, sessionId, wavedToUser);

        this.favorite = favorite;
    }


    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public int getSessionId() { return sessionId; }

    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public String getName() { return name; }

    public void setWavedToUser(boolean wavedToUser) { this.wavedToUser = wavedToUser; }

    public boolean getWavedToUser() { return wavedToUser; }

    public void setWavedFromUser(boolean wavedFromUser) { this.wavedFromUser = wavedFromUser; }

    public boolean getWavedFromUser() { return wavedFromUser; }

    public void setName(String name) { this.name = name; }

    public String getHeadshotURL() { return headshotURL; }

    public void setHeadshotURL(String headshotURL) { this.headshotURL = headshotURL; }

    public boolean isFavorite() { return favorite; }

    public void setFavorite(boolean favorite) { this.favorite = favorite; }

}
