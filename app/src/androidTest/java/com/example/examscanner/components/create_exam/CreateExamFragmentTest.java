package com.example.examscanner.components.create_exam;

import android.view.View;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.StateFullTest;

import org.hamcrest.Matcher;
import org.junit.After;
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
import static org.hamcrest.Matchers.containsString;
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
        final String term = "1";
        final String aGraderAdress = "rotba@post.bgu.ac.il";
        testCreateOneExam(comp, year, verNum, term, aGraderAdress);
    }
    @Test
    public void testOn2CreatedExamItAddsToTheHomeAdapter() {
        testOnCreatedExamItAddsToTheHomeAdapter();
        onView(withId(R.id.floating_button_home_create_exam)).perform(click());
        final String comp = "SPL";
        final String year = "2019";
        final String verNum = "3";
        final String term = "2";
        final String aGraderAdress = "rotba@post.bgu.ac.il";
        testCreateOneExam(comp, year, verNum, term, aGraderAdress);
    }

    private void testCreateOneExam(String comp, String year, String verNum, String term, String aGraderAdress) {
        onView(withId(R.id.editText_create_exam_course_name)).perform(replaceText(comp));
        onView(withId(R.id.radioButton_create_exam_term_b)).perform(click());
        onView(withId(R.id.radioButton_create_exam_semester_fall)).perform(click());
        onView(withId(R.id.editText_create_exam_year)).perform(replaceText(year));
        onView(withId(R.id.editText_create_exam_version_number)).perform(replaceText(verNum));
        onView(withId(R.id.button_create_exam_upload_version_image)).perform(click());
        onView(withId(R.id.editText_create_exam_grader_address)).perform(replaceText(aGraderAdress));
        onView(withId(R.id.button_create_exam_create)).perform(click());
        onView(withText(R.string.create_exam_dialog_ok)).perform(click());
        onView(withText(containsString(comp))).check(matches(isDisplayed()));
        onView(withText(containsString(year))).check(matches(isDisplayed()));
        onView(withText(containsString(term))).check(matches(isDisplayed()));
    }
}