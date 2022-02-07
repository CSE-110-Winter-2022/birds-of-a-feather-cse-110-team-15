package com.example.birdsofafeather;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birdsofafeather.R;
import com.squareup.picasso.Picasso;

public class InputHeadshotActivity extends AppCompatActivity{
    EditText editURL;
    Button saveBtn, continueBtn;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_headshot);

        editURL = findViewById(R.id.editURL);
        saveBtn = findViewById(R.id.saveBtn);
        continueBtn = findViewById(R.id.continueBtn);



    }

    //https://guides.codepath.com/android/Displaying-Images-with-the-Picasso-Library
    public void onSaveClick(View view) {
        profile = findViewById(R.id.profile);
        String URL = editURL.getText().toString();
        Picasso.get().load(URL)
                .into(profile);
    }

}
