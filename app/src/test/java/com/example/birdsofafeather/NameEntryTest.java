package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.AlertDialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowAlertDialog;

@RunWith(AndroidJUnit4.class)
public class NameEntryTest {

    @Test
    /*
    Test no name
    Steps: Input no name-> click confirm button -> error -> click continue -> error
     -> input name -> click confirm
    */
    public void test_name_after_no_name() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserNameActivity.class);

        try(ActivityScenario<UserNameActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);

                //set name in edit text to no name
                EditText EditConfirm = activity.findViewById(R.id.input_name_textview);
                EditConfirm.setText("");

                Button confirmButton = activity.findViewById(R.id.confirm);
                confirmButton.performClick();

                // Robolectric function to return most recently created alert
                AlertDialog alert = ShadowAlertDialog.getLatestAlertDialog();
                assertTrue(alert.isShowing());

                // Dismiss alert
                Button alertButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                alertButton.performClick();

                //get what's in textfield
                TextView confirmName = activity.findViewById(R.id.name_view);
                String getConfirmName = confirmName.getText().toString();
                assertEquals("", getConfirmName);

                //check that continue with no name results in alert
                AlertDialog alertCont = ShadowAlertDialog.getLatestAlertDialog();
                assertTrue(alertCont.isShowing());

                // Dismiss alert
                Button alertButtonCont = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                alertButtonCont.performClick();

                //input new name and click confirm
                EditConfirm.setText("Nachelle");
                confirmButton.performClick();

                //get name from text view and check
                String getNewName = confirmName.getText().toString();
                assertEquals("Nachelle", getNewName);
            });
        }
    }

    @Test
    /*
    Test one first name
    Steps: Input name-> click confirm button
    */
    public void test_one_name(){
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserNameActivity.class);

        try(ActivityScenario<UserNameActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);

                EditText confirmName = activity.findViewById(R.id.input_name_textview);
                confirmName.setText("Nachelle");

                //click confirm button to confirm name
                Button confirmButton = activity.findViewById(R.id.confirm);
                confirmButton.performClick();

                //get name in text field
                TextView confirmed_name_view = activity.findViewById(R.id.name_view);
                String getConfirmName = confirmed_name_view.getText().toString();

                assertEquals("Nachelle", getConfirmName);
            });
        }
    }

    @Test
    /*
    Test two first names
    Steps: Input name-> click confirm button
    */
    public void test_name_with_space(){
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserNameActivity.class);

        try(ActivityScenario<UserNameActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);

                //set edit text
                EditText confirmName = activity.findViewById(R.id.input_name_textview);
                confirmName.setText("Nachelle Azhley");

                //click confirm button to confirm name
                Button confirmButton = activity.findViewById(R.id.confirm);
                confirmButton.performClick();

                //get name in text field
                TextView confirmed_name_view = activity.findViewById(R.id.name_view);
                String getConfirmName = confirmed_name_view.getText().toString();

                assertEquals("Nachelle Azhley", getConfirmName);
            });
        }
    }

    @Test
    /*
    Test non letters name
    Steps: Input name-> click confirm button
    */
    public void test_name_with_non_letters(){
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserNameActivity.class);

        try(ActivityScenario<UserNameActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);

                //set edit text
                EditText confirmName = activity.findViewById(R.id.input_name_textview);
                confirmName.setText("X AE A-12");

                //click confirm button to confirm name
                Button confirmButton = activity.findViewById(R.id.confirm);
                confirmButton.performClick();

                //get name in text field
                TextView confirmed_name_view = activity.findViewById(R.id.name_view);
                String getConfirmName = confirmed_name_view.getText().toString();

                assertEquals("X AE A-12", getConfirmName);
            });
        }
    }
}
