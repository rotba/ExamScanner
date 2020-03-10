package com.example.examscanner;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public abstract class StateFullTest extends AbstractComponentInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityScenarioRule=
            new ActivityScenarioRule<MainActivity>(MainActivity.class);
    @Before
    @Override
    public void setUp() {
        super.setUp();
        login();
    }

    public int getCurrentGraderId(){
        return State.getState().getGraderId();
    }
    private void login(){onView(withId(R.id.button_login)).perform(click());}

    @After
    public void tearDown() throws Exception {
        State.getState().logout();
    }
}
