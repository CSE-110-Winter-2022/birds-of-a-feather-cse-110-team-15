package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birdsofafeather.models.db.AppDatabase;

public class MainActivity extends AppCompatActivity {
    GoogleLoginHandler loginHandler;
    AppDatabase db;
    Button loginButton;
    Button createProfileButton;
    Button favoriteListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginHandler = new GoogleLoginHandler(this);

        loginButton = (Button) findViewById(R.id.Login_Button);
        createProfileButton = (Button) findViewById(R.id.Create_Profile_Button);
        favoriteListButton = (Button) findViewById(R.id.Favorites_Button);
        db = AppDatabase.singleton(this);
        setButtonVisibilities();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!loginHandler.isUserSignedIn()) {
            loginHandler.signIn();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setButtonVisibilities();
    }


    public void onLoginClick(View view) {
        Intent intent = new Intent(this, StartStopSearchActivity.class);
        startActivity(intent);
    }

    public void onCreateProfile(View view) {
        Intent intent = new Intent(this, UserNameActivity.class);
        intent.putExtra("uuid", new UUIDManager(getApplicationContext()).getUserUUID());
        startActivity(intent);
    }

    // set the visibility of buttons
    public void setButtonVisibilities() {
        // if the database has no data, show create profile button and
        // hide other buttons
        if (db.studentWithCoursesDao().count() == 0){
            loginButton.setVisibility(View.INVISIBLE);
            createProfileButton.setVisibility((View.VISIBLE));
            favoriteListButton.setVisibility((View.INVISIBLE));

        }
        // else show login button and other buttons
        else {
            loginButton.setVisibility(View.VISIBLE);
            createProfileButton.setVisibility((View.INVISIBLE));
            favoriteListButton.setVisibility((View.VISIBLE));
        }
    }

    public void onFavoriteListClick(View view) {
        Intent intent = new Intent(this, FavoriteListActivity.class);
        startActivity(intent);
    }
}