package com.example.birdsofafeather;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.CourseDao;
import com.example.birdsofafeather.models.db.Student;
import com.example.birdsofafeather.models.db.StudentWithCoursesDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class SeeListOfClassmatesTest {
    private StudentWithCoursesDao studentCoursesDao;
    private CourseDao coursesDao;
    private AppDatabase db;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db.useTestSingleton(context);
        db = AppDatabase.singleton(context);
    }

    @After
    public void closeDb() throws IOException{
        db.close();
    }

    @Test
    public void writeStudentsAndSeeList() throws Exception{
        Student Nachelle = new Student ("Nachelle");
        Student Kathy = new Student ("Kathy");

        Course nCourse = new Course(Nachelle.getStudentId(), );
        Course kCourse = new Course(Kathy.getStudentId(), );

        db.studentWithCoursesDao().insert(Nachelle);
        db.studentWithCoursesDao().insert(Kathy);


    }


}
