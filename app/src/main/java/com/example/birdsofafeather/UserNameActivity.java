package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class UserNameActivity extends AppCompatActivity{
    private boolean confirmButtonClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
        TextView ConfirmName = (TextView) findViewById(R.id.name_view);
        String nameString = ConfirmName.getText().toString();
        ConfirmName.setVisibility(View.INVISIBLE);

        // If name has not been set, and user is signed in
        if (nameString.isEmpty()) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            GoogleNameGetter get = new GoogleNameGetter(account, findViewById(R.id.input_name_textview));

        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onConfirmNameClick(View view) {
        confirmButtonClick = true;

        //get input name
        TextView confirmName = findViewById(R.id.input_name_textview);
        String getConfirmName = confirmName.getText().toString();


        //check if user entered a name
        if(getConfirmName.equals("")){
            //pop up error message
            Utilities.showAlert(this, "Please enter a name.");
        } else {
            //show name
            TextView showConfirmName = (TextView) findViewById(R.id.name_view);
            showConfirmName.setVisibility(View.VISIBLE);
            showConfirmName.setText(getConfirmName);
        }

    }

    public void onContinueNameClick(View view){
        //get the intent
        Intent intent = new Intent(this, InputHeadshotActivity.class);
        TextView InputUserNameView = findViewById(R.id.name_view);
        String getInputUserNameView = InputUserNameView.getText().toString();

        //check if user entered a name
        if(getInputUserNameView.equals("")){
            //pop up error message
            Utilities.showAlert(this, "Please enter a name.");
            return;
        } else if(confirmButtonClick == false) {
            //pop up error message
            Utilities.showAlert(this, "Please click confirm first.");
            return;
        }

        //set extra
        intent.putExtra("student_name", getInputUserNameView);
        startActivity(intent);
        finish();
    }
}



