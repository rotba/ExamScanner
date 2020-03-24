package com.example.examscanner;

import com.example.examscanner.components.scan_exam.StateFullTest;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class MainActivityTest extends StateFullTest {

    @Test
    public void testAfterLoginHomeIsDisplayed() {
        onView(withId(R.id.home)).check(matches(isDisplayed()));
    }
}