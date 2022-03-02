package com.example.birdsofafeather.models.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudentWithCoursesDao {
   @Transaction
   @Query("SELECT * FROM students")
   List<StudentWithCourses> getAll();

   @Query("SELECT * FROM students WHERE uuid=:uuid")
   StudentWithCourses get(String uuid);

   @Query("SELECT * FROM students WHERE uuid=:uuid AND session_id=:sessionId")
   StudentWithCourses getStudentWithSession(String uuid, int sessionId);

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   long insert(Student student);

   @Delete
   void delete(Student student);

   @Update
   void updateStudent(Student student);

   @Query("UPDATE students SET favorite=:favorite WHERE uuid=:uuid")
   void updateFavorite(String uuid, boolean favorite);

   @Query("UPDATE students SET wavedFromUser=:waveFrom WHERE uuid=:uuid")
   void updateWaveFrom(String uuid, boolean waveFrom);

   @Query("UPDATE students SET wavedToUser=:waveTo WHERE uuid=:uuid")
   void updateWaveTo(String uuid, boolean waveTo);

   @Query("SELECT COUNT(*) FROM students")
   int count();

   @Query("SELECT last_insert_rowid()")
   int lastIdCreated();

   @Query("SELECT * FROM students WHERE favorite=1")
   List<StudentWithCourses> getFavorites();
}
