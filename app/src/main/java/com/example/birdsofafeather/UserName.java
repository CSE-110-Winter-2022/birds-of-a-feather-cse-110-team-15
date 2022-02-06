package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class UserName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
        loadProfile();
    }

    @Override
    protected void onDestroy() {
        saveProfile();
        super.onDestroy();
    }

    public void loadProfile() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        EditText nameView = (EditText) findViewById(R.id.name_textview);
        String name = preferences.getString("name","");

        nameView.setText(name, TextView.BufferType.EDITABLE);

    }

    public void saveProfile() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        EditText nameView = (EditText) findViewById(R.id.name_textview);
        editor.putString("name", nameView.getText().toString());

        editor.apply();
    }

    public void onConfirmNameClick(View view) {

        finish();
    }
}