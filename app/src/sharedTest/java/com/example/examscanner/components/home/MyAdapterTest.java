package com.example.examscanner.components.home;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;


import com.example.examscanner.MainActivity;
import com.example.examscanner.R;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
;


@RunWith(AndroidJUnit4.class)
public class MyAdapterTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);
    @Before
    public void setUp(){
        MainActivity activity = Robolectric.setupActivity(MyActivity.class);
    }
    @Test
    public void recyclerViewHasCompATest() {
        onView(ViewMatchers.withId(R.id.bLogin))
                .perform(ViewActions.click());
        onView(withText("COMP A")).check(matches(isDisplayed()));
    }

    private void navigateToHome() {
        onView(ViewMatchers.withId(R.id.bLogin))
                .perform(ViewActions.click());
    }
}