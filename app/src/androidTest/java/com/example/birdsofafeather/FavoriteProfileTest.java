package com.example.birdsofafeather;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static java.lang.System.out;


import android.content.Context;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.TextView;


import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.Student;
import com.example.birdsofafeather.models.db.StudentWithCourses;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FavoriteProfileTest {

    @Before
    public void init() {
        Context context = ApplicationProvider.getApplicationContext();
        AppDatabase.useTestSingleton(context);
        AppDatabase db = AppDatabase.singleton(context);

        db.studentWithCoursesDao().insert(new Student("Bob", "bob.com"));
        db.studentWithCoursesDao().insert(new Student("Bill", "bill.com"));
        db.studentWithCoursesDao().insert(new Student("Mary", "mary.com", true));
        StudentWithCourses s1 = db.studentWithCoursesDao().get(3);

        // Bob's classes
        db.coursesDao().insert(new Course(1, "CSE 20 FA 2021"));
        db.coursesDao().insert(new Course(1, "CSE 100 FA 2021"));

        // Bill's class (Has 2, shares 1)
        db.coursesDao().insert(new Course(2, "CSE 20 FA 2021"));
        db.coursesDao().insert(new Course(2, "CSE 15L FA 2021"));

        // Mary's classes (Has 2, shares 2)
        db.coursesDao().insert(new Course(3, "CSE 20 FA 2021"));
        db.coursesDao().insert(new Course(3, "CSE 100 FA 2021"));
    }

    @Test
    /* Tests favorite checkbox and other elements show up on profile page if they exist */
    public void testFavoriteFromProfile() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ViewProfileActivity.class);
        intent.putExtra("classmate_id", 3);
        try (ActivityScenario<ViewProfileActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                TextView name = activity.findViewById(R.id.name_view);
                CheckBox fav = activity.findViewById(R.id.profile_favorite);

                ViewInteraction checkname = onView(
                        withId(R.id.name_view)).
                            check(matches(withText("Mary")));
                checkname.check(matches(isDisplayed()));

                ViewInteraction FavCheckBox = onView(
                        withId(R.id.profile_favorite)).
                            check(matches(isClickable()));
                FavCheckBox.check(matches(isDisplayed()));

                ViewInteraction FavCheckBoxChecked = onView(
                        withId(R.id.profile_favorite)).
                            check(matches(isChecked()));
                FavCheckBoxChecked.perform(click());

//                assertEquals("Mary", name.getText());
//                assertTrue(fav.isChecked());
//                out.println("Expected: True    Actual:" + fav.isChecked());
                    }
            );
        }
    }
}
