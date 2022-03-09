package com.example.birdsofafeather.models.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface CourseDao {
    @Transaction
    @Query("SELECT * FROM courses WHERE student_id=:studentId")
    List<Course> getForStudent(String studentId);

    @Query("SELECT * FROM courses WHERE id=:id")
    Course get(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Course course);

    @Query("SELECT * FROM courses WHERE name=:name AND student_id=:uuid")
    Course getCourseWithStudent(String name, String uuid);

    @Delete
    void delete(Course course);

    @Query("SELECT COUNT(*) FROM courses")
    int count();

    @Query("SELECT last_insert_rowid()")
    int lastIdCreated();
}
