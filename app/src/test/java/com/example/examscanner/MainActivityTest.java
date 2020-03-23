package com.example.examscanner;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import static android.os.Looper.getMainLooper;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<AuthenticationActivity> auth
            = new ActivityScenarioRule<AuthenticationActivity>(AuthenticationActivity.class);
    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityScenarioRule
            = new ActivityScenarioRule<MainActivity>(MainActivity.class);


    @After
    public void tearDown() throws Exception {
        State.getState().logout();
    }

    @Test
    public void test() {
        onView(withId(R.id.button_login)).perform(click());
        Utils.sleepSwipingTime();
        Utils.sleepSwipingTime();
        onView(withId(R.id.home)).check(matches(isDisplayed()));
    }
}