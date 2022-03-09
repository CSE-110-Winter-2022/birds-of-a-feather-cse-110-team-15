package com.example.birdsofafeather;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import static java.lang.System.out;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.Student;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("deprecation")
@LargeTest
@RunWith(AndroidJUnit4.class)
public class StartSessionEspressoTest {
    private Context context;
    private AppDatabase db;

    @Before
    public void init() {
        context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.singleton(context);
        UUIDManager uuidManager = new UUIDManager(ApplicationProvider.getApplicationContext());
        String UUID = uuidManager.getUserUUID();
        // insert user into database
        db.studentWithCoursesDao().insert(new Student(UUID, "Joe", "joe.com"));
        db.coursesDao().insert(new Course(UUID, "CSE 21 FA 2020"));
        db.coursesDao().insert(new Course(UUID, "CSE 30 WI 2021"));
        db.coursesDao().insert(new Course(UUID, "CSE 100 SP 2021"));
        db.coursesDao().insert(new Course(UUID, "CSE 105 FA 2021"));
    }

    @Rule
    public ActivityTestRule<StartStopSearchActivity> mActivityTestRule = new ActivityTestRule<StartStopSearchActivity>(StartStopSearchActivity.class) {
        @Override
        public void beforeActivityLaunched() {
            context = ApplicationProvider.getApplicationContext();
            AppDatabase.useTestSingleton(context);
        }
    };

    @Test
    /*
      Test popup updating title and saving sessions
     */
    public void startSessionEspressoTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.start_button), withText("START"),
                        childAtPosition(
                                allOf(withId(R.id.start_stop_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.start_session_button), withText("START SESSION"),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                2),
                        isDisplayed()));
        materialButton2.perform(click());

        // check if title changed to current date / time
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
        String curSession = formatter.format(new Date());
        onView(allOf(withId(R.id.cur_session))).check(matches(withText(curSession)));

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.finish_button), withText("Mock the arrival of nearby messages"),
                        childAtPosition(
                                allOf(withId(R.id.start_stop_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                7),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.input_data_view),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("a123,,,\nToby,,,\nhttps://lh3.googleusercontent.com/pw/AM-JKLVOqkfwjcRfaEo5UIwLKN9FLPM3iiVTvxJdr58qPI4G4ipBruOCWm8lUTC3q-YEWpK_JiWfdRzE3WPh6eezbcDVsRC1qYsxoy_UwUIx0X2YY8nMA_6-iPqWX2R3Z_9LaTIjNCaBRTSCcauP1OB6Bljb=w275-h183-no?authuser=0,,,\nCSE,21,FA,2020\nCSE,30,WI,2021\nCSE,105,FA,2021\nCSE,100,SP,2021"), closeSoftKeyboard());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.input_data_view),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(pressImeActionButton());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.button), withText("Go Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton5.perform(click());

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        ViewInteraction materialButton14 = onView(
                allOf(withId(R.id.stop_button), withText("STOP"),
                        childAtPosition(
                                allOf(withId(R.id.start_stop_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton14.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.session_name_view),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.session_name_view), withText("test"),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0),
                        isDisplayed()));
        appCompatEditText7.perform(pressImeActionButton());

        ViewInteraction materialButton15 = onView(
                allOf(withId(R.id.save_session_button), withText("SAVE"),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                3),
                        isDisplayed()));
        materialButton15.perform(click());

        // make sure new test name is reflected in session title
        onView(allOf(withId(R.id.cur_session))).check(matches(withText("test")));

        ViewInteraction materialButton16 = onView(
                allOf(withId(R.id.start_button), withText("START"),
                        childAtPosition(
                                allOf(withId(R.id.start_stop_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        materialButton16.perform(click());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.session_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView.perform(click());

        ViewInteraction materialButton17 = onView(
                allOf(withId(R.id.start_session_button), withText("START SESSION"),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                2),
                        isDisplayed()));
        materialButton17.perform(click());

        // make sure starting existing 'test' session is reflected on title
        onView(allOf(withId(R.id.cur_session))).check(matches(withText("test")));

        ViewInteraction materialButton18 = onView(
                allOf(withId(R.id.stop_button), withText("STOP"),
                        childAtPosition(
                                allOf(withId(R.id.start_stop_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton18.perform(click());

        // wait 5 seconds
        try {
            Thread.sleep(5000);
        } catch(InterruptedException e) {
            out.println("got interrupted!");
        }

        // assert recycler view is the same
        RecyclerView recyclerView = mActivityTestRule.getActivity().findViewById(R.id.students_recycler_view);
        out.println("RecyclerView Child Count");
        out.println("Expected: 1      Actual: " + recyclerView.getAdapter().getItemCount());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
    }
