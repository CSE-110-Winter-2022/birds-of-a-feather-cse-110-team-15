package com.example.birdsofafeather;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.widget.Button;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
public class NameEntryTest {
    @Rule
    public ActivityScenarioRule<UserNameActivity> scenarioRule = new ActivityScenarioRule<>(UserNameActivity.class);

    @Test
    public void no_name_entered() {
        // Create a "scenario" to move through the activity lifecycle.
        ActivityScenario<UserNameActivity> scenario = scenarioRule.getScenario();

        // Make sure the activity is in the created state
        scenario.moveToState(Lifecycle.State.CREATED);

        // walk through
        scenario.onActivity(activity -> {
            Button otherButton = activity.findViewById(R.id.confirm);
            otherButton.performClick(); //no input

            //get what's in textfield
            TextView confirmName =  activity.findViewById(R.id.input_name_textview);
            String getConfirmName = confirmName.getText().toString();
            assertEquals("", getConfirmName);
        });
    }

//    @Test
//    public void test_first_name(){
//
//    }
//
//    @Test
//    public void test_name_with_space(){
//
//    }
//
//    @Test
//    public void test_long_name(){
//
//    }
//
//    @Test
//    public void test_one_letter_name(){
//
//    }
//
//    @Test
//    public void test_name_with_non_letters(){
//
//    }
//
//    @Test
//    public void test_name_with_numbers(){
//
//    }

}
