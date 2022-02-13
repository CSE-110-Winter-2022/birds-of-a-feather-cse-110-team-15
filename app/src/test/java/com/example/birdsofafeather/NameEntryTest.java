package com.example.birdsofafeather;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class NameEntryTest {

    @Test
    /*
    Test no name
    Steps: Input no name-> click confirm button
    */
    public void no_name_entered() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserNameActivity.class);

        try(ActivityScenario<UserNameActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);

                Button confirmButton = activity.findViewById(R.id.confirm);
                confirmButton.performClick(); //no input

                //get what's in textfield
                TextView confirmName = activity.findViewById(R.id.name_view);
                String getConfirmName = confirmName.getText().toString();
                assertEquals("", getConfirmName);
            });
        }
    }

    @Test
    /*
    Test input name after no name
    Steps: Input no name-> click confirm button -> input name -> click confirm
    */
    public void name_after_no_name() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserNameActivity.class);

        try(ActivityScenario<UserNameActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);

                Button confirmButton = activity.findViewById(R.id.confirm);
                confirmButton.performClick(); //no input

                //get what's in textfield
                TextView confirmName = activity.findViewById(R.id.name_view);
                confirmName.setText("Nachelle"); //set name to Nachelle
                String getConfirmName = confirmName.getText().toString();
                assertEquals("Nachelle", getConfirmName);
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
                confirmName.setText("Nachelle"); //set name to Nachelle

                //get name in text field
                String getConfirmName = confirmName.getText().toString();

                //click confirm button to confirm name
                Button confirmButton = activity.findViewById(R.id.confirm);
                confirmButton.performClick(); //no input

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

                EditText confirmName = activity.findViewById(R.id.input_name_textview);
                confirmName.setText("Nachelle Azhley"); //set name to Nachelle Azhley

                //get name in text field
                String getConfirmName = confirmName.getText().toString();

                //click confirm button to confirm name
                Button confirmButton = activity.findViewById(R.id.confirm);
                confirmButton.performClick(); //no input

                assertEquals("Nachelle Azhley", getConfirmName);
            });
        }
    }


    @Test
    /*
    Test one letter name
    Steps: Input name-> click confirm button
    */
    public void test_one_letter_name(){
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserNameActivity.class);

        try(ActivityScenario<UserNameActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                scenario.moveToState(Lifecycle.State.CREATED);

                EditText confirmName = activity.findViewById(R.id.input_name_textview);
                confirmName.setText("Z"); //set name to Z

                //get name in text field
                String getConfirmName = confirmName.getText().toString();

                //click confirm button to confirm name
                Button confirmButton = activity.findViewById(R.id.confirm);
                confirmButton.performClick(); //no input

                assertEquals("Z", getConfirmName);
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

                EditText confirmName = activity.findViewById(R.id.input_name_textview);
                confirmName.setText("X AE A-12"); //set name to X AE A-12

                //get name in text field
                String getConfirmName = confirmName.getText().toString();

                //click confirm button to confirm name
                Button confirmButton = activity.findViewById(R.id.confirm);
                confirmButton.performClick(); //no input

                assertEquals("X AE A-12", getConfirmName);
            });
        }
    }

}
