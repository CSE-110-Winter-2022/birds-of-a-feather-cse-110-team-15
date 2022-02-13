package com.example.birdsofafeather;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso, GoogleSigninOptions.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END build_client]

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isUserSignedIn()) {
            signIn();
        }
    }

    // Check for Google Sign In
    private boolean isUserSignedIn () {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        return account != null;
    }

    // Google Sign In fuction
    // Mostly sample code from developers.google.com
    // https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInClient
    private void signIn() {
        // Attempt silent sign-in
        Task<GoogleSignInAccount> task = mGoogleSignInClient.silentSignIn();
        if (!task.isSuccessful()) {
            // There's no immediate result ready, waits for the async callback.
            task.addOnCompleteListener(task1 -> {
                try {
                    task1.getResult(ApiException.class);
                } catch (ApiException apiException) {
                    // You can get from apiException.getStatusCode() the detailed error code
                    // e.g. GoogleSignInStatusCodes.SIGN_IN_REQUIRED means user needs to take
                    // explicit action to finish sign-in;
                    // Please refer to GoogleSignInStatusCodes Javadoc for details
                    if (apiException.getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_REQUIRED) {
                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                    } else {
                        // The ApiException status code indicates the detailed failure reason.
                        // Please refer to the GoogleSignInStatusCodes class reference for more information.
                        Log.w(TAG, "signInResult:failed code=" + apiException.getStatusCode());
                    }
                }
            });
        }
    }


    public void onLoginClick(View view) {
        Intent intent = new Intent(this, StartStopSearchActivity.class);
        startActivity(intent);
    }

    public void onCreateProfile(View view) {
        Intent intent = new Intent(this, UserNameActivity.class);
        startActivity(intent);
    }
}