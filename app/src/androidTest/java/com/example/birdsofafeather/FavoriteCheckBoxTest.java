package com.example.birdsofafeather;


import static org.junit.Assert.assertEquals;

import android.view.View;
import android.content.Intent;
import android.widget.CheckBox;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Student;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FavoriteCheckBoxTest {
    Student s1;
    Student s2;

    @BeforeClass
    public void init(){
        AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
        s1 = new Student("Bob", "bob.com");
        s2 = new Student("Bill", "bill.com");
        db.studentWithCoursesDao().insert(s1);
        db.studentWithCoursesDao().insert(s2);
    }

    @Test
    public void testFavorite(){
        s1.setFavorite(true);
        assertEquals(true, s1.isFavorite());
        assertEquals(false, s2.isFavorite());
    }

//    @Test
//    public void testCheckBox_1(){
//        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), StartStopSearchActivity.class);
//        intent.putExtra("classmate_id", 1);
//        try(ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(intent)){
//            scenario.onActivity(activity -> {
//                s1.setFavorite(true);
//                CheckBox fav = activity.findViewById(R.id.favorite);
//                assertEquals(true, fav.isChecked());
//            });
//        }
//    }
//
//    @Test
//    public void testCheckBox_2(){
//        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ViewProfileActivity.class);
//        intent.putExtra("classmate_id", 2);
//        try(ActivityScenario<StartStopSearchActivity> scenario = ActivityScenario.launch(intent)){
//            scenario.onActivity(activity -> {
//                s1.setFavorite(true);
//                CheckBox fav = activity.findViewById(R.id.favorite_1);
//                assertEquals(true, fav.isChecked());
//            });
//        }
//    }

}
