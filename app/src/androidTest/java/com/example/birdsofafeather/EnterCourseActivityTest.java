package com.example.birdsofafeather;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
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
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.birdsofafeather.models.db.AppDatabase;
import com.example.birdsofafeather.models.db.Course;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.ListIterator;

@SuppressWarnings("deprecation")
@LargeTest
@RunWith(AndroidJUnit4.class)
public class EnterCourseActivityTest {

    @Rule
    public ActivityTestRule<EnterCourseActivity> mActivityTestRule = new ActivityTestRule<>(EnterCourseActivity.class);

    @Before
    public void clearCourses() {
        AppDatabase db = AppDatabase.singleton(InstrumentationRegistry.getInstrumentation().getTargetContext());
        List<Course> courseList= db.coursesDao().getForStudent(1);
        for (ListIterator<Course> iter = courseList.listIterator(); iter.hasNext(); ) {
            db.coursesDao().delete(iter.next());
            iter.remove();
        }
    }

    @Test
    public void enterCourseActivityTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.finish_button), withText("Done"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(android.R.id.message), withText("Please enter at least one course."),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction materialButton2 = onView(
                allOf(withId(android.R.id.button1), withText("Ok"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton2.perform(scrollTo(), click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(android.R.id.message), withText("Please fill in every field."),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

        ViewInteraction materialButton4 = onView(
                allOf(withId(android.R.id.button1), withText("Ok"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton4.perform(scrollTo(), click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.course_subject_textview),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("CSE"), closeSoftKeyboard());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(android.R.id.message), withText("Please fill in every field."),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
                        isDisplayed()));
        textView3.check(matches(isDisplayed()));

        ViewInteraction materialButton6 = onView(
                allOf(withId(android.R.id.button1), withText("Ok"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton6.perform(scrollTo(), click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.course_subject_textview), withText("CSE"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText(""));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.course_subject_textview),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.course_number_textview),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("110"), closeSoftKeyboard());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton7.perform(click());

        ViewInteraction textView4 = onView(
                allOf(withId(android.R.id.message), withText("Please fill in every field."),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
                        isDisplayed()));
        textView4.check(matches(isDisplayed()));

        ViewInteraction materialButton8 = onView(
                allOf(withId(android.R.id.button1), withText("Ok"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton8.perform(scrollTo(), click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.course_subject_textview),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("C"), closeSoftKeyboard());

        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton9.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(android.R.id.message), withText("Please enter a 2-4 letter subject name.\n(e.g. SE or COGS)"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
                        isDisplayed()));
        textView5.check(matches(isDisplayed()));

        ViewInteraction materialButton10 = onView(
                allOf(withId(android.R.id.button1), withText("Ok"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton10.perform(scrollTo(), click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.course_subject_textview), withText("C"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("C1"));

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.course_subject_textview), withText("C1"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText7.perform(closeSoftKeyboard());

        ViewInteraction materialButton11 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton11.perform(click());

        ViewInteraction textView6 = onView(
                allOf(withId(android.R.id.message), withText("Please enter a 2-4 letter subject name.\n(e.g. SE or COGS)"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
                        isDisplayed()));
        textView6.check(matches(isDisplayed()));

        ViewInteraction materialButton12 = onView(
                allOf(withId(android.R.id.button1), withText("Ok"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton12.perform(scrollTo(), click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.course_subject_textview), withText("C1"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("CSETL"));

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.course_subject_textview), withText("CSETL"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText9.perform(closeSoftKeyboard());

        ViewInteraction materialButton13 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton13.perform(click());

        ViewInteraction textView7 = onView(
                allOf(withId(android.R.id.message), withText("Please enter a 2-4 letter subject name.\n(e.g. SE or COGS)"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
                        isDisplayed()));
        textView7.check(matches(isDisplayed()));

        ViewInteraction materialButton14 = onView(
                allOf(withId(android.R.id.button1), withText("Ok"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton14.perform(scrollTo(), click());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.course_subject_textview), withText("CSETL"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText10.perform(replaceText("MATH"));

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.course_subject_textview), withText("MATH"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText11.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.course_number_textview), withText("110"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText12.perform(replaceText("020"));

        ViewInteraction appCompatEditText13 = onView(
                allOf(withId(R.id.course_number_textview), withText("020"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText13.perform(closeSoftKeyboard());

        ViewInteraction materialButton15 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton15.perform(click());

        ViewInteraction textView8 = onView(
                allOf(withId(android.R.id.message), withText("Please enter a valid course number.\n(e.g. 100 or 15L)"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
                        isDisplayed()));
        textView8.check(matches(isDisplayed()));

        ViewInteraction materialButton16 = onView(
                allOf(withId(android.R.id.button1), withText("Ok"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton16.perform(scrollTo(), click());

        ViewInteraction appCompatEditText14 = onView(
                allOf(withId(R.id.course_number_textview), withText("020"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText14.perform(replaceText("3111"));

        ViewInteraction appCompatEditText15 = onView(
                allOf(withId(R.id.course_number_textview), withText("3111"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText15.perform(closeSoftKeyboard());

        ViewInteraction materialButton17 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton17.perform(click());

        ViewInteraction textView9 = onView(
                allOf(withId(android.R.id.message), withText("Please enter a valid course number.\n(e.g. 100 or 15L)"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
                        isDisplayed()));
        textView9.check(matches(isDisplayed()));

        ViewInteraction materialButton18 = onView(
                allOf(withId(android.R.id.button1), withText("Ok"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton18.perform(scrollTo(), click());

        ViewInteraction appCompatEditText16 = onView(
                allOf(withId(R.id.course_number_textview), withText("3111"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText16.perform(replaceText("31AH"));

        ViewInteraction appCompatEditText17 = onView(
                allOf(withId(R.id.course_number_textview), withText("31AH"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText17.perform(closeSoftKeyboard());

        ViewInteraction materialButton19 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton19.perform(click());

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.course_row_text), withText("MATH 31AH FA 2022"),
                        withParent(allOf(withId(R.id.frameLayout),
                                withParent(withId(R.id.courses_recycler_view)))),
                        isDisplayed()));
        textView10.check(matches(withText("MATH 31AH FA 2022")));

        ViewInteraction appCompatEditText18 = onView(
                allOf(withId(R.id.course_number_textview), withText("31AH"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText18.perform(click());

        ViewInteraction appCompatEditText19 = onView(
                allOf(withId(R.id.course_number_textview), withText("31AH"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText19.perform(replaceText("31BH"));

        ViewInteraction appCompatEditText20 = onView(
                allOf(withId(R.id.course_number_textview), withText("31BH"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText20.perform(closeSoftKeyboard());

        ViewInteraction materialButton20 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton20.perform(click());

        ViewInteraction textView11 = onView(
                allOf(withId(R.id.course_row_text), withText("MATH 31AH FA 2022"),
                        withParent(allOf(withId(R.id.frameLayout),
                                withParent(withId(R.id.courses_recycler_view)))),
                        isDisplayed()));
        textView11.check(matches(withText("MATH 31AH FA 2022")));

        ViewInteraction textView12 = onView(
                allOf(withId(R.id.course_row_text), withText("MATH 31BH FA 2022"),
                        withParent(allOf(withId(R.id.frameLayout),
                                withParent(withId(R.id.courses_recycler_view)))),
                        isDisplayed()));
        textView12.check(matches(withText("MATH 31BH FA 2022")));

        ViewInteraction materialButton21 = onView(
                allOf(withId(R.id.remove_course_button), withText("X"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout),
                                        childAtPosition(
                                                withId(R.id.courses_recycler_view),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton21.perform(click());

        ViewInteraction materialButton22 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton22.perform(click());

        ViewInteraction textView13 = onView(
                allOf(withId(android.R.id.message), withText("This course is already entered"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
                        isDisplayed()));
        textView13.check(matches(isDisplayed()));

        ViewInteraction materialButton23 = onView(
                allOf(withId(android.R.id.button1), withText("Ok"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton23.perform(scrollTo(), click());

        ViewInteraction appCompatEditText21 = onView(
                allOf(withId(R.id.course_number_textview), withText("31BH"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText21.perform(replaceText("31AH"));

        ViewInteraction appCompatEditText22 = onView(
                allOf(withId(R.id.course_number_textview), withText("31AH"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText22.perform(closeSoftKeyboard());

        ViewInteraction materialButton24 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton24.perform(click());

        ViewInteraction textView14 = onView(
                allOf(withId(R.id.course_row_text), withText("MATH 31BH FA 2022"),
                        withParent(allOf(withId(R.id.frameLayout),
                                withParent(withId(R.id.courses_recycler_view)))),
                        isDisplayed()));
        textView14.check(matches(withText("MATH 31BH FA 2022")));

        ViewInteraction textView15 = onView(
                allOf(withId(R.id.course_row_text), withText("MATH 31AH FA 2022"),
                        withParent(allOf(withId(R.id.frameLayout),
                                withParent(withId(R.id.courses_recycler_view)))),
                        isDisplayed()));
        textView15.check(matches(withText("MATH 31AH FA 2022")));

        ViewInteraction appCompatEditText23 = onView(
                allOf(withId(R.id.course_subject_textview), withText("MATH"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText23.perform(replaceText("SE"));

        ViewInteraction appCompatEditText24 = onView(
                allOf(withId(R.id.course_subject_textview), withText("SE"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText24.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText25 = onView(
                allOf(withId(R.id.course_number_textview), withText("31AH"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText25.perform(replaceText("130L"));

        ViewInteraction appCompatEditText26 = onView(
                allOf(withId(R.id.course_number_textview), withText("130L"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText26.perform(closeSoftKeyboard());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.quarter_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.year_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatSpinner2.perform(click());

        DataInteraction appCompatCheckedTextView2 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(5);
        appCompatCheckedTextView2.perform(click());

        ViewInteraction materialButton25 = onView(
                allOf(withId(R.id.enter_button), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton25.perform(click());

        ViewInteraction appCompatEditText27 = onView(
                allOf(withId(R.id.course_number_textview), withText("130L"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText27.perform(pressImeActionButton());

        ViewInteraction materialButton26 = onView(
                allOf(withId(R.id.remove_course_button), withText("X"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout),
                                        childAtPosition(
                                                withId(R.id.courses_recycler_view),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton26.perform(click());

        ViewInteraction materialButton27 = onView(
                allOf(withId(R.id.remove_course_button), withText("X"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout),
                                        childAtPosition(
                                                withId(R.id.courses_recycler_view),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton27.perform(click());

        ViewInteraction materialButton28 = onView(
                allOf(withId(R.id.finish_button), withText("Done"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton28.perform(click());
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
