package com.example.birdsofafeather;

import android.os.Bundle;
import android.util.Pair;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.StudentWithCourses;

import java.util.List;

public class FavoriteListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        AppDatabase db = AppDatabase.singleton(this);
        List<Pair<StudentWithCourses, Integer>> studentAndCountPairList;
        List<StudentWithCourses> favorites = db.studentWithCoursesDao().getFavorites();

        FavoriteViewAdapter studentsViewAdapter;

        // Set up the RecycleView for the list of students
        RecyclerView studentsRecycleView = findViewById(R.id.favorites_recycler_view);
        RecyclerView.LayoutManager studentsLayoutManager = new LinearLayoutManager(this);
        studentsRecycleView.setLayoutManager(studentsLayoutManager);

        // Pass in student list and function to update favorite status to the adapter
        studentsViewAdapter = new FavoriteViewAdapter(favorites);
        studentsRecycleView.setAdapter(studentsViewAdapter);

    }


}