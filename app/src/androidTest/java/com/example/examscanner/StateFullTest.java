package com.example.examscanner;

import androidx.test.espresso.matcher.ViewMatchers;

import com.example.examscanner.state.StateFactory;

import org.junit.After;
import org.junit.Before;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


public abstract class StateFullTest extends AbstractComponentInstrumentedTest {
    private static final String TAG = "StateFullTest";

    @Before
    @Override
    public void setUp() {
        super.setUp();
        login();
    }



    private void login() {
        onView(ViewMatchers.withId(R.id.button_login)).perform(click());
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        mainActivityScenarioRule.getScenario().onActivity(activity -> activity.logOut());
    }
}
