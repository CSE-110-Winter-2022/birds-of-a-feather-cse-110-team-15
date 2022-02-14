package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
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

    @Test
    /*
    Insert one student and no classes in common
     */
    public void insertOneStudent() throws Exception{
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), StartStopSearchActivity.class);
        intent.putExtra("student_name", "");

        try(ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);

                //start mocking
                Button startMock = activity.findViewById(R.id.finish_button);
                startMock.performClick(); //no input

                //insert student with no classes in common
                EditText inputStudentInfo = activity.findViewById(R.id.input_data_view);
                inputStudentInfo.setText("Bill,,,\n" +
                        "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,\n" +
                        "CSE,210,FA,2021\n" +
                        "CSE,110,WI,2022\n" +
                        "CSE,110,SP,2022");

                //click enter and go back to start_stop page
                Button enterList = activity.findViewById(R.id.enter_button);
                enterList.performClick();

                //check one person is in db
                assertEquals(1, db.studentWithCoursesDao().getAll().size());

                Button goBack = activity.findViewById(R.id.button);

                //click start and stop button which should show list
                Button start_button = activity.findViewById(R.id.start_button);
                Button stop_button = activity.findViewById(R.id.stop_button);

                //
                assertEquals("Bill", db.studentWithCoursesDao().getAll().get(0).getName());


            });
        }
    }

//    @Test
//    /*
//    Insert one student and 1 related courses
//     */
//    public void insertOneStudent() throws Exception{
//        //create student and insert into db
//        Student Bill = new Student ("Bill",SAMPLE_URL);
//        db.studentWithCoursesDao().insert(Bill);
//        Bill.setStudentId(db.studentWithCoursesDao().lastIdCreated());
//
//        //create student courses and insert into db
//        Course nCourse = new Course(11111111, "CSE 111");
//        db.coursesDao().insert(nCourse);
//        nCourse.setCourseId(db.coursesDao().lastIdCreated());
//
//        //check one person is in db
//        assertEquals(1, db.studentWithCoursesDao().getAll().size());
//
//        //


        //click mock test
        //add list of people
        //check if people are there


//    @Test
//    /*
//    Insert one students and 2 related courses out of 6
//    */
//    public void insertThreeStudents() throws Exception{
//
//    }




}
