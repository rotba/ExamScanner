package com.example.examscanner.components.scan_exam;

import android.util.Log;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.examscanner.MainActivity;
import com.example.examscanner.R;
import com.example.examscanner.State;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


public abstract class StateFullTest extends AbstractComponentInstrumentedTest {
    private static final String TAG = "StateFullTest";
    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityScenarioRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);


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
        State.getState().logout();
    }
}
