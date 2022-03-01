package com.example.birdsofafeather.models.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.birdsofafeather.models.db.Session;
import com.example.birdsofafeather.models.db.SessionWithStudents;

import java.util.List;

@Dao
public interface SessionWithStudentsDao {
    @Transaction
    @Query("SELECT * FROM sessions")
    List<SessionWithStudents> getAll();

    @Query("SELECT * FROM sessions WHERE id=:id")
    SessionWithStudents get(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Session session);

    @Delete
    void delete(Session session);

    @Update
    void updateSession(Session session);

    @Query("SELECT COUNT(*) FROM sessions")
    int count();
}
