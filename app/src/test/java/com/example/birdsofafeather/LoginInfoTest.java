package com.example.birdsofafeather;

import static org.mockito.Mockito.when;

import android.test.mock.MockContext;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public class LoginInfoTest {
    @Mock
    GoogleSignInAccount signInAccount;

    @Test
    public void nameReturned () {
        TextView view = Mockito.spy(new TextView(new MockContext()));

        when(signInAccount.getGivenName()).thenReturn("Testee");

        GoogleNameGetter getter = new GoogleNameGetter(signInAccount, view);

        assert(view.toString().equals("Testee"));
    }
}
