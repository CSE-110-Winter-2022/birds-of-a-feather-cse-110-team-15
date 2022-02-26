package com.example.birdsofafeather.models.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sessions")
public class Session {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int sessionId;

    @ColumnInfo(name = "name")
    private String name;

    public Session(String name) {
        this.name = name;
    }

    public int getSessionId() { return sessionId; }

    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
