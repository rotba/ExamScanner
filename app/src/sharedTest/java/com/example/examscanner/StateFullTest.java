package com.example.examscanner;

import android.app.Activity;
import android.util.Log;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
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
    private BaseLoaderCallback mLoaderCallback;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        mainActivityScenarioRule.getScenario().onActivity(activity -> {
            mLoaderCallback = new BaseLoaderCallback(activity) {
                @Override
                public void onManagerConnected(int status) {
                    switch (status) {
                        case LoaderCallbackInterface.SUCCESS: {
                            Log.i("MainActivity", "OpenCV loaded successfully");
                        }
                        break;
                        default: {
                            super.onManagerConnected(status);
                        }
                        break;
                    }
                }
            };
        });
        mainActivityScenarioRule.getScenario().onActivity(activity -> {
            if (!OpenCVLoader.initDebug()) {
                Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
                OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, activity, mLoaderCallback);
            } else {
                Log.d(TAG, "OpenCV library found inside package. Using it!");
                mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            }
        });
        login();
    }

    public int getCurrentGraderId() {
        return State.getState().getGraderId();
    }

    private void login() {
        onView(withId(R.id.button_login)).perform(click());
    }

    @After
    public void tearDown() throws Exception {
        State.getState().logout();
    }
}
