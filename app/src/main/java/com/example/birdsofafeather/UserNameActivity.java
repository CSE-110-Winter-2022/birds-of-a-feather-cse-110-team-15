package com.example.birdsofafeather;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class UserNameActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
        // loadProfile();
    }

    @Override
    protected void onDestroy() {
        //  saveProfile();
        super.onDestroy();
    }



    public void onConfirmNameClick(View view) {
//        Context context = view.getContext();
//        Intent intent = new Intent(context, .class);
//        intent.putExtra("person_id", this.person.getId());
//        context.startActivity(intent);
        finish();
    }
}



