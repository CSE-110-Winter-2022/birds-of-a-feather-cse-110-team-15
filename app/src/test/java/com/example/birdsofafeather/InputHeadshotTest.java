package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.AlertDialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowAlertDialog;


@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion=30)
public class InputHeadshotTest {
    String DEFAULT_URL = "https://lh3.googleusercontent.com/pw/AM-JKLUTkMaSnWQDXiRUw7FdrFk7lu" +
            "oo6VSJqafn8K1Bh1QksFJiO1oOjV5EoUbWnHc7xKtxDGeD9l8R6a7xtdfMFu4iz2y6QovxF0n4e3hZNG" +
            "cq1izg_XLtUlX-BStPmG1FnGj9VW0wwoOy5G-i4VaNPA9I=s800-no?authuser=0";

    String SAMPLE_URL = "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PS" +
            "Urijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51" +
            "nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0";

    String BAD_URL = "bad-link.com";

    @Test
    /*
    Test loading a valid URL after first trying a bad one
    Steps: Type bad URL -> Press Save -> Type good URL -> Press Save -> Check image
    */
    public void testLoadValidURL() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), InputHeadshotActivity.class);
        intent.putExtra("student_name", "Test Student");
        try(ActivityScenario<InputHeadshotActivity> scenario = ActivityScenario.launch(intent)){
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);
                EditText url_view = activity.findViewById(R.id.editURL);
                ImageView profile = activity.findViewById(R.id.profile);
                Button save_button = activity.findViewById(R.id.saveBtn);
                Button continue_button = activity.findViewById(R.id.continueBtn);

                // Trying to save a bad URL
                url_view.setText(BAD_URL);
                save_button.performClick();

                // Now try to save a good URL
                url_view.setText(SAMPLE_URL);
                save_button.performClick();
                assertEquals(profile.getTag(), SAMPLE_URL);

            });
        }
    }

    @Test
    /* Test continue without uploading an image URL.
    Steps: Press Continue -> Check for alert -> Press Continue -> Check activity finished
    */
    public void testContinueEmptyURL() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), InputHeadshotActivity.class);
        intent.putExtra("student_name", "Test Student");
        try(ActivityScenario<InputHeadshotActivity> scenario = ActivityScenario.launch(intent)){
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);

                ImageView profile = activity.findViewById(R.id.profile);
                Button continue_button = activity.findViewById(R.id.continueBtn);

                // Continue without entering URL
                continue_button.performClick();

                // Robolectric function to return most recently created alert
                AlertDialog alert = ShadowAlertDialog.getLatestAlertDialog();
                assertTrue(alert.isShowing());

                // Dismiss alert
                Button alertButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                alertButton.performClick();

                // Check image is default URL
                assertEquals(profile.getTag(), DEFAULT_URL);

                // Continue and check that activity is finished
                continue_button.performClick();
                assertTrue(activity.isFinishing());
            });
        }
    }
}


