package com.example.examscanner.components.scan_exam;

import androidx.test.espresso.matcher.ViewMatchers;

import com.example.examscanner.R;

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

    public int getCurrentGraderId() {
        return State.getState().getGraderId();
    }

    private void login() {
        onView(ViewMatchers.withId(R.id.button_login)).perform(click());
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        State.getState().logout();
    }
}
