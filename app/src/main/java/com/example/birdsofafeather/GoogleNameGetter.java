package com.example.birdsofafeather;

import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class GoogleNameGetter {
    GoogleSignInAccount account;
    TextView confirmName;

    // On creation, saves account object passed, as well as TextView that will be changed with the name
    public GoogleNameGetter (@Nullable GoogleSignInAccount account, TextView confirmName) {
        this.account = account;
        this.confirmName = confirmName;
        updateName(account, confirmName);
    }

    // If account object exists, get the given name from the profile and set name textview to it
    private void updateName(@Nullable GoogleSignInAccount account, TextView confirmName) {
        String getConfirmName = confirmName.getText().toString();

        if (account != null && getConfirmName.isEmpty()) {
            String toPutIn = account.getGivenName();
            confirmName.setText(toPutIn);
        }
    }

    // Returns string in TextView held by object
    public String getName() {
        return confirmName.getText().toString();
    }
}
