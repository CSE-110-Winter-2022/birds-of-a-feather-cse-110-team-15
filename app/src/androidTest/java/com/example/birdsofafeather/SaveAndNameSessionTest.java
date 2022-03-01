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

import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import static org.junit.Assert.assertEquals;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;
import com.example.birdsofafeather.models.db.SessionWithStudents;
import com.example.birdsofafeather.models.db.Student;
import com.example.birdsofafeather.models.db.StudentWithCourses;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SaveAndNameSessionTest {
    AppDatabase db;

    @Rule
    public ActivityTestRule<UserNameActivity> mActivityTestRule = new ActivityTestRule<UserNameActivity>(UserNameActivity.class){
        @Override
        public void beforeActivityLaunched() {
            Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            AppDatabase.useTestSingleton(context);
            db = AppDatabase.singleton(context);
        }
    };

    @Test
    public void saveSessionTest3() {

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.input_name_textview),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.confirm), withText("CONFIRM"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.cont), withText("CONTINUE"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.continueBtn), withText("Continue"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(android.R.id.button1), withText("Ok"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton4.perform(scrollTo(), click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.continueBtn), withText("Continue"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.course_subject_textview),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("CSE"), closeSoftKeyboard());

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

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.course_number_textview), withText("21"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("30"));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.course_number_textview), withText("30"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText5.perform(closeSoftKeyboard());

        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.quarter_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatSpinner2.perform(click());

        DataInteraction appCompatCheckedTextView2 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatSpinner3 = onView(
                allOf(withId(R.id.year_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatSpinner3.perform(click());

        DataInteraction appCompatCheckedTextView3 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView3.perform(click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton7.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.course_number_textview), withText("30"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("105"));

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.course_number_textview), withText("105"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText7.perform(closeSoftKeyboard());

        ViewInteraction appCompatSpinner4 = onView(
                allOf(withId(R.id.year_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatSpinner4.perform(click());

        DataInteraction appCompatCheckedTextView4 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(0);
        appCompatCheckedTextView4.perform(click());

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton8.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.course_number_textview), withText("105"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("110"));

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.course_number_textview), withText("110"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText9.perform(closeSoftKeyboard());

        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton9.perform(click());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.course_number_textview), withText("110"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText10.perform(closeSoftKeyboard());

        ViewInteraction materialButton10 = onView(
                allOf(withId(R.id.finish_button), withText("Done"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton10.perform(click());

        ViewInteraction materialButton11 = onView(
                allOf(withId(R.id.start_button), withText("START"),
                        childAtPosition(
                                allOf(withId(R.id.start_stop_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        materialButton11.perform(click());

        ViewInteraction materialButton12 = onView(
                allOf(withId(R.id.start_session_button), withText("START SESSION"),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                2),
                        isDisplayed()));
        materialButton12.perform(click());

        ViewInteraction materialButton13 = onView(
                allOf(withId(R.id.finish_button), withText("Mock the arrival of nearby messages"),
                        childAtPosition(
                                allOf(withId(R.id.start_stop_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                7),
                        isDisplayed()));
        materialButton13.perform(click());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.input_data_view),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText11.perform(replaceText("Bob,,,\nhttps://lh3.googleusercontent.com/pw/AM-JKLWsX79MvqlAcyBXqHMtW5QY0T8LUhXg2dwFzA1Oi8zWPTt8o2l2iR2Rfpa_MgkuBpGJtlnJ9OKb5M5IIRrXgZEYKe4YCyuyKLeyncIVKP5n1dbFdhdkLkYURga3Gu7TWsB2tmWaalAYRXvxV0P61fpC=w612-h408-no?authuser=0,,,\nWCWP,10A,SP,2020\nCSE,110,WI,2022\nCSE,12,FA,2020"), closeSoftKeyboard());

        ViewInteraction materialButton14 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton14.perform(click());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.input_data_view),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText12.perform(pressImeActionButton());

        ViewInteraction materialButton15 = onView(
                allOf(withId(R.id.button), withText("Go Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton15.perform(click());

        SystemClock.sleep(3000); // wait for 3 seconds for student list to show up

        ViewInteraction materialButton16 = onView(
                allOf(withId(R.id.stop_button), withText("STOP"),
                        childAtPosition(
                                allOf(withId(R.id.start_stop_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton16.perform(click());

        ViewInteraction appCompatSpinner5 = onView(
                allOf(withId(R.id.current_courses_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatSpinner5.perform(click());

        DataInteraction appCompatCheckedTextView5 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(2);
        appCompatCheckedTextView5.perform(click());

        ViewInteraction materialButton17 = onView(
                allOf(withId(R.id.save_session_button), withText("SAVE"),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                3),
                        isDisplayed()));
        materialButton17.perform(click());

        ViewInteraction materialButton18 = onView(
                allOf(withId(R.id.start_button), withText("START"),
                        childAtPosition(
                                allOf(withId(R.id.start_stop_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        materialButton18.perform(click());

        ViewInteraction materialButton19 = onView(
                allOf(withId(R.id.start_session_button), withText("START SESSION"),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                2),
                        isDisplayed()));
        materialButton19.perform(click());

        ViewInteraction materialButton20 = onView(
                allOf(withId(R.id.finish_button), withText("Mock the arrival of nearby messages"),
                        childAtPosition(
                                allOf(withId(R.id.start_stop_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                7),
                        isDisplayed()));
        materialButton20.perform(click());

        ViewInteraction appCompatEditText13 = onView(
                allOf(withId(R.id.input_data_view),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText13.perform(replaceText("Toby,,,\nhttps://lh3.googleusercontent.com/pw/AM-JKLVOqkfwjcRfaEo5UIwLKN9FLPM3iiVTvxJdr58qPI4G4ipBruOCWm8lUTC3q-YEWpK_JiWfdRzE3WPh6eezbcDVsRC1qYsxoy_UwUIx0X2YY8nMA_6-iPqWX2R3Z_9LaTIjNCaBRTSCcauP1OB6Bljb=w275-h183-no?authuser=0,,,\nCSE,21,FA,2020\nCSE,30,WI,2021\nCSE,105,WI,2022\nCSE,110,WI,2022"), closeSoftKeyboard());

        ViewInteraction materialButton21 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton21.perform(click());

        ViewInteraction appCompatEditText14 = onView(
                allOf(withId(R.id.input_data_view),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText14.perform(pressImeActionButton());

        ViewInteraction materialButton22 = onView(
                allOf(withId(R.id.button), withText("Go Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton22.perform(click());

        SystemClock.sleep(3000); // wait for 3 seconds for student list to show up

        ViewInteraction materialButton23 = onView(
                allOf(withId(R.id.stop_button), withText("STOP"),
                        childAtPosition(
                                allOf(withId(R.id.start_stop_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton23.perform(click());

        ViewInteraction appCompatEditText15 = onView(
                allOf(withId(R.id.session_name_view),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0),
                        isDisplayed()));
        appCompatEditText15.perform(replaceText("CSE 105"), closeSoftKeyboard());

        ViewInteraction appCompatEditText16 = onView(
                allOf(withId(R.id.session_name_view), withText("CSE 105"),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0),
                        isDisplayed()));
        appCompatEditText16.perform(pressImeActionButton());

        ViewInteraction materialButton24 = onView(
                allOf(withId(R.id.save_session_button), withText("SAVE"),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                3),
                        isDisplayed()));
        materialButton24.perform(click());

        ViewInteraction materialButton25 = onView(
                allOf(withId(R.id.start_button), withText("START"),
                        childAtPosition(
                                allOf(withId(R.id.start_stop_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        materialButton25.perform(click());

        ViewInteraction materialButton26 = onView(
                allOf(withId(R.id.start_session_button), withText("START SESSION"),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                2),
                        isDisplayed()));
        materialButton26.perform(click());

        ViewInteraction materialButton27 = onView(
                allOf(withId(R.id.finish_button), withText("Mock the arrival of nearby messages"),
                        childAtPosition(
                                allOf(withId(R.id.start_stop_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                7),
                        isDisplayed()));
        materialButton27.perform(click());

        ViewInteraction appCompatEditText17 = onView(
                allOf(withId(R.id.input_data_view),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText17.perform(replaceText("Mary,,,\nhttps://lh3.googleusercontent.com/pw/AM-JKLUqD5Rbb9riO8QnmAJf6f216BvPqPJnEWxrT5X4jMsahJ2CajVYyEKk7Z-bm0rNT3i9E_w6dAhv42b-P80quY_MMWg5fmJZBItJny894ygxE4HK-eWfV8jRXJ2FooPrXwYl3xRn5hQRtlswAfjnE9ti=w540-h360-no?authuser=0,,,\nCSE,15L,FA,2020\nCSE,30,WI,2021\nCSE,105,WI,2022"), closeSoftKeyboard());

        ViewInteraction materialButton28 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton28.perform(click());

        ViewInteraction appCompatEditText18 = onView(
                allOf(withId(R.id.input_data_view),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText18.perform(pressImeActionButton());

        ViewInteraction materialButton29 = onView(
                allOf(withId(R.id.button), withText("Go Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton29.perform(click());

        SystemClock.sleep(3000); // wait for 3 seconds for student list to show up

        ViewInteraction materialButton30 = onView(
                allOf(withId(R.id.stop_button), withText("STOP"),
                        childAtPosition(
                                allOf(withId(R.id.start_stop_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton30.perform(click());

        ViewInteraction materialButton31 = onView(
                allOf(withId(R.id.save_session_button), withText("SAVE"),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                3),
                        isDisplayed()));
        materialButton31.perform(click());

        ViewInteraction materialButton32 = onView(
                allOf(withId(R.id.start_button), withText("START"),
                        childAtPosition(
                                allOf(withId(R.id.start_stop_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        materialButton32.perform(click());

        ViewInteraction appCompatSpinner6 = onView(
                allOf(withId(R.id.session_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatSpinner6.perform(click());

        DataInteraction appCompatCheckedTextView6 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3);
        appCompatCheckedTextView6.perform(click());

        ViewInteraction materialButton33 = onView(
                allOf(withId(R.id.start_session_button), withText("START SESSION"),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                2),
                        isDisplayed()));
        materialButton33.perform(click());

        // manually get name because this is time when the test is running
        String curSession = db.sessionWithStudentsDao().get(3).getName();

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.cur_session), withText(curSession),
                        childAtPosition(
                                allOf(withId(R.id.start_stop_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction appCompatEditText19 = onView(
                allOf(withId(R.id.session_name_view),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0),
                        isDisplayed()));
        appCompatEditText19.perform(replaceText("CSE 30"), closeSoftKeyboard());

        ViewInteraction materialButton34 = onView(
                allOf(withId(R.id.save_session_button), withText("SAVE"),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                3),
                        isDisplayed()));
        materialButton34.perform(click());

        ViewInteraction materialButton35 = onView(
                allOf(withId(R.id.stop_button), withText("STOP"),
                        childAtPosition(
                                allOf(withId(R.id.start_stop_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton35.perform(click());

        // get session data from database and check if session is properly saved
        List<SessionWithStudents> sessions = db.sessionWithStudentsDao().getAll();
        List<StudentWithCourses> students;  // student list in session
        // session 1
        students = sessions.get(0).getStudents();
        assertEquals("CSE 110 WI 2022", sessions.get(0).getName());
        assertEquals("Bob", students.get(0).getName());
        System.out.println("Expected: CSE 110 WI 2022   Actual: " + sessions.get(0).getName());
        System.out.println("Expected: Bob               Actual: " + students.get(0).getName());

        // session 2
        students = sessions.get(1).getStudents();
        assertEquals("CSE 105", sessions.get(1).getName());
        assertEquals("Toby", students.get(0).getName());
        System.out.println("Expected: CSE 105           Actual: " + sessions.get(1).getName());
        System.out.println("Expected: Toby              Actual: " + students.get(0).getName());

        // session 3 (after renaming)
        students = sessions.get(2).getStudents();
        assertEquals("CSE 30", sessions.get(2).getName());
        assertEquals("Mary", students.get(0).getName());
        System.out.println("Expected: CSE 30            Actual: " + sessions.get(2).getName());
        System.out.println("Expected: Mary              Actual: " + students.get(0).getName());
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
