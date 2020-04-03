package com.example.examscanner.components.create_exam;

import android.view.View;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.StateFullTest;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class CreateExamFragmentTest  extends StateFullTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
        onView(withId(R.id.floating_button_home_create_exam)).perform(click());
    }

    @Test
    public void testOnCreatedExamItAddsToTheHomeAdapter() {
        final String comp = "COMP";
        final String year = "2020";
        final String verNum = "3";
        final String aGraderAdress = "rotba@post.bgu.ac.il";
        onView(withId(R.id.editText_create_exam_course_name)).perform(replaceText(comp));
        onView(withId(R.id.radioButton_create_exam_term_b)).perform(click());
        onView(withId(R.id.radioButton_create_exam_semester_fall)).perform(click());
        onView(withId(R.id.editText_create_exam_year)).perform(replaceText(year));
        onView(withId(R.id.editText_create_exam_version_number)).perform(replaceText(verNum));
        onView(withId(R.id.button_create_exam_upload_version_image)).perform(click());
        onView(withId(R.id.editText_create_exam_grader_address)).perform(replaceText(aGraderAdress));
        onView(withId(R.id.button_create_exam_create)).perform(click());
        onView(withText(R.string.create_exam_dialog_ok)).perform(click());
        onView(withText(comp)).check(matches(isDisplayed()));
        onView(withText(verNum)).check(matches(isDisplayed()));
        onView(withText(year)).check(matches(isDisplayed()));
    }
}