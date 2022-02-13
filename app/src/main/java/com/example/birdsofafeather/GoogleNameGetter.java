package com.example.birdsofafeather;

import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.w3c.dom.Text;

public class GoogleNameGetter {
    GoogleSignInAccount account;
    TextView view;

    public GoogleNameGetter (GoogleSignInAccount account, TextView view) {
        this.account = account;
        updateName(account, view);
    }

    private void updateName(@Nullable GoogleSignInAccount account, TextView confirmName) {
        String getConfirmName = confirmName.getText().toString();

        if (account != null && getConfirmName.isEmpty()) {
            confirmName.setText(account.getGivenName());
        }
    }

    public String getName() {
        return view.getText().toString();
    }
}
