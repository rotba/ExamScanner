package com.example.examscanner;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityScenarioRule=
            new ActivityScenarioRule<MainActivity>(MainActivity.class);



    @Test
    public void testAfterLoginHomeIsDisplayed() {
        onView(withId(R.id.button_login)).perform(click());
        onView(withId(R.id.home)).check(matches(isDisplayed()));
    }
}