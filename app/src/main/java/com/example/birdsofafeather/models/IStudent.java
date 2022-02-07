package com.example.birdsofafeather.models;

import android.widget.ImageView;

import java.util.List;

public interface IStudent {
    int getId();
    String getName();
    String getHeadshotURL();

    List<String> getCourses();
}
