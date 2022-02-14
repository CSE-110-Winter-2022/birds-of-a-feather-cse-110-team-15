package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Intent;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion=30)
public class LoginInfoTest {
    final String EX_NAME = "Testee";

    @Test
    public void testNameReturned() {
        // Given a name by Google Account, name should be the same as the name given
        final GoogleSignInAccount signInAccount = mock(GoogleSignInAccount.class);
        when(signInAccount.getGivenName()).thenReturn(EX_NAME);

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserNameActivity.class);
        try(ActivityScenario<UserNameActivity> scenario = ActivityScenario.launch(intent)){
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);
                TextView nameInput = activity.findViewById(R.id.input_name_textview);

                new GoogleNameGetter(signInAccount, nameInput);

                // Now try to save a good URL
                assertEquals(nameInput.getText().toString(), EX_NAME);
            });
        }
    }

    @Test
    public void testNoName() {
        // Given no name by Google Account, name should remain blank
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserNameActivity.class);
        try(ActivityScenario<UserNameActivity> scenario = ActivityScenario.launch(intent)){
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);
                TextView nameInput = activity.findViewById(R.id.input_name_textview);

                new GoogleNameGetter(null, nameInput);

                // Now try to save a good URL
                assert(nameInput.getText().toString().isEmpty());
            });
        }
    }
}
