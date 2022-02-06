package com.example.birdsofafeather;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.ViewProfileActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Instrumented test, which will execute on an Android device.

 */
@RunWith(AndroidJUnit4.class)
public class ViewProfileActivityTest {

    @Rule
    public ActivityScenarioRule<ViewProfileActivity> rule =
            new ActivityScenarioRule<ViewProfileActivity>(ViewProfileActivity.class);

    @Test
    public void testProfileLoad(){
        // Multiple assertions in one test to avoid launching too many activities
        ActivityScenario<ViewProfileActivity> scenario = rule.getScenario();
        scenario.onActivity(activity -> {
            TextView name = (TextView)activity.findViewById(R.id.name_view);
            ImageView picture = (ImageView)activity.findViewById(R.id.profile_picture_view);
            TextView courses = (TextView)activity.findViewById(R.id.common_classes_view);
            // Test name loaded
            assert(name.getText().equals("Bill"));

            // Test picture loaded
            assert(picture.getTag() != null);

            // Test course loaded and visibility
            assert(courses.getVisibility() == View.VISIBLE);
            assert(courses.getText().equals("CSE 100 SP 2021\n"));
        });
    }
}