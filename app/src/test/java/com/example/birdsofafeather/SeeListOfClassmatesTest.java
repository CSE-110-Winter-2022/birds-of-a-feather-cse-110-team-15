package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

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

    String DEFAULT_URL = "https://lh3.googleusercontent.com/pw/AM-JKLUTkMaSnWQDXiRUw7FdrFk7lu" +
            "oo6VSJqafn8K1Bh1QksFJiO1oOjV5EoUbWnHc7xKtxDGeD9l8R6a7xtdfMFu4iz2y6QovxF0n4e3hZNG" +
            "cq1izg_XLtUlX-BStPmG1FnGj9VW0wwoOy5G-i4VaNPA9I=s800-no?authuser=0";

    String SAMPLE_URL = "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PS" +
            "Urijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51" +
            "nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0";

    String BAD_URL = "bad-link.com";

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

//    @Test
//    /*
//    Insert one student, no related courses
//     */
//    public void insertOneStudent_noRelated() throws Exception{
//
//    }

    @Test
    /*
    Insert one student and 1 related courses
     */
    public void insertOneStudent() throws Exception{
        //create student and insert into db
        Student Bill = new Student ("Bill",SAMPLE_URL);
        db.studentWithCoursesDao().insert(Bill);
        Bill.setStudentId(db.studentWithCoursesDao().lastIdCreated());

        //create student courses and insert into db
        Course nCourse = new Course(11111111, "CSE 111");
        db.coursesDao().insert(nCourse);
        nCourse.setCourseId(db.coursesDao().lastIdCreated());

        //check one person is in db
        assertEquals(1, db.studentWithCoursesDao().getAll().size());

        //
    }

//    @Test
//    /*
//    Insert one students and 2 related courses out of 6
//    */
//    public void insertThreeStudents() throws Exception{
//
//    }




}
