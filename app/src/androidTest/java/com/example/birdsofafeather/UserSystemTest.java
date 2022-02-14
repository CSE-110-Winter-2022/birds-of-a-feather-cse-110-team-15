package com.example.birdsofafeather;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UserSystemTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() throws InterruptedException {
        try {
            ViewInteraction materialButton = onView(
                    allOf(withId(R.id.Create_Profile_Button), withText("Create Profile"),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    1),
                            isDisplayed()));
            materialButton.perform(click());

            ViewInteraction materialButton2 = onView(
                    allOf(withId(R.id.confirm), withText("CONFIRM"),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    4),
                            isDisplayed()));
            materialButton2.perform(click());

            ViewInteraction materialButton3 = onView(
                    allOf(withId(R.id.cont), withText("CONTINUE"),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    3),
                            isDisplayed()));
            materialButton3.perform(click());

            ViewInteraction materialButton4 = onView(
                    allOf(withId(R.id.saveBtn), withText("Save"),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    2),
                            isDisplayed()));
            materialButton4.perform(click());

            ViewInteraction materialButton5 = onView(
                    allOf(withId(android.R.id.button1), withText("Ok"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.ScrollView")),
                                            0),
                                    3)));
            materialButton5.perform(scrollTo(), click());

            ViewInteraction materialButton6 = onView(
                    allOf(withId(R.id.continueBtn), withText("Continue"),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    3),
                            isDisplayed()));
            materialButton6.perform(click());

            ViewInteraction appCompatEditText = onView(
                    allOf(withId(R.id.course_subject_textview),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    1),
                            isDisplayed()));
            appCompatEditText.perform(replaceText("cse"), closeSoftKeyboard());

            ViewInteraction appCompatEditText2 = onView(
                    allOf(withId(R.id.course_number_textview),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    2),
                            isDisplayed()));
            appCompatEditText2.perform(click());

            ViewInteraction appCompatEditText3 = onView(
                    allOf(withId(R.id.course_number_textview),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    2),
                            isDisplayed()));
            appCompatEditText3.perform(replaceText("21"), closeSoftKeyboard());

            ViewInteraction appCompatSpinner = onView(
                    allOf(withId(R.id.year_spinner),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    4),
                            isDisplayed()));
            appCompatSpinner.perform(click());

            DataInteraction appCompatCheckedTextView = onData(anything())
                    .inAdapterView(childAtPosition(
                            withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                            0))
                    .atPosition(2);
            appCompatCheckedTextView.perform(click());

            ViewInteraction materialButton7 = onView(
                    allOf(withId(R.id.enter_button), withText("Enter"),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    5),
                            isDisplayed()));
            materialButton7.perform(click());

            ViewInteraction appCompatEditText4 = onView(
                    allOf(withId(R.id.course_number_textview), withText("21"),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    2),
                            isDisplayed()));
            appCompatEditText4.perform(pressImeActionButton());

            ViewInteraction materialButton8 = onView(
                    allOf(withId(R.id.finish_button), withText("Done"),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    6),
                            isDisplayed()));
            materialButton8.perform(click());
        } catch (Exception e) {
            ViewInteraction materialButton = onView(
                    allOf(withId(R.id.Login_Button), withText("Login"),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    2),
                            isDisplayed()));
            materialButton.perform(click());
        }

        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.start_button), withText("START"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton9.perform(click());

        ViewInteraction materialButton10 = onView(
                allOf(withId(R.id.finish_button), withText("Mock the arrival of nearby messages"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton10.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.input_data_view),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("Bob,,,\nhttps://lh3.googleusercontent.com/pw/AM-JKLWsX79MvqlAcyBXqHMtW5QY0T8LUhXg2dwFzA1Oi8zWPTt8o2l2iR2Rfpa_MgkuBpGJtlnJ9OKb5M5IIRrXgZEYKe4YCyuyKLeyncIVKP5n1dbFdhdkLkYURga3Gu7TWsB2tmWaalAYRXvxV0P61fpC=w612-h408-no?authuser=0,,,\nWCWP,10A,SP,2020\nCSE,110,SP,2022\nCSE,21,FA,2020"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.input_data_view), withText("Bob,,,\nhttps://lh3.googleusercontent.com/pw/AM-JKLWsX79MvqlAcyBXqHMtW5QY0T8LUhXg2dwFzA1Oi8zWPTt8o2l2iR2Rfpa_MgkuBpGJtlnJ9OKb5M5IIRrXgZEYKe4YCyuyKLeyncIVKP5n1dbFdhdkLkYURga3Gu7TWsB2tmWaalAYRXvxV0P61fpC=w612-h408-no?authuser=0,,,\nWCWP,10A,SP,2020\nCSE,110,SP,2022\nCSE,21,FA,2020"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText6.perform(pressImeActionButton());

        ViewInteraction materialButton11 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton11.perform(click());

        ViewInteraction materialButton12 = onView(
                allOf(withId(R.id.button), withText("Go Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton12.perform(click());

        Thread.sleep(5000);

        ViewInteraction materialButton13 = onView(
                allOf(withId(R.id.stop_button), withText("STOP"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton13.perform(click());
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
