package com.example.birdsofafeather.models.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SessionWithStudents {
    @Embedded
    public Session session;

    @Relation(parentColumn = "id", entityColumn = "session_id", entity = Student.class)
    public List<StudentWithCourses> students;

    public Session getSession() { return session; }

    public int getSessionId() { return session.getSessionId(); }

    public void setSessionId(int sessionId) { session.setSessionId(sessionId); }

    public String getName() { return session.getName(); }

    public void setName(String name) { session.setName(name); }

    public List<StudentWithCourses> getStudents() { return students; }
}
