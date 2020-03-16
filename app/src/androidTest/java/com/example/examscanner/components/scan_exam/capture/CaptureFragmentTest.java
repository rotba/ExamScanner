package com.example.examscanner.components.scan_exam.capture;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.AbstractComponentInstrumentedTest;
import com.example.examscanner.MainActivity;
import com.example.examscanner.R;
import com.example.examscanner.StateFullTest;

import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.components.scan_exam.Utils.sleepCameraPreviewSetupTime;
import static com.example.examscanner.components.scan_exam.Utils.sleepSingleCaptureProcessingTime;
import static com.example.examscanner.components.scan_exam.Utils.sleepSingleCaptureTakingTime;

@RunWith(AndroidJUnit4.class)
public class CaptureFragmentTest {



    @Before
    public void setUp() {
        FragmentScenario<CaptureFragment> scenario =
                FragmentScenario.launchInContainer(CaptureFragment.class);
    }

    @Test
    public void testTheNumberOfUnprocessedCapturesUpdates() {
        assertUserSeeProgress(0,0);
        sleepCameraPreviewSetupTime();
        onView(withId(R.id.capture_image_button)).perform(click());
        sleepSingleCaptureTakingTime();
        try {
            assertUserSeeProgress(0,1);
        }catch (NoMatchingViewException e){
            assertUserSeeProgress(1,1);
        }
    }

    @Test
    public void testTheNumberOfProcessedAndUnprocessedCapturesUpdates() {
        assertUserSeeProgress(0,0);
        sleepCameraPreviewSetupTime();
        onView(withId(R.id.capture_image_button)).perform(click());
        sleepSingleCaptureProcessingTime();
        assertUserSeeProgress(1,1);
    }

    private void assertUserSeeProgress(int processed, int outOf) {
        onView(withText(xOutOfY(processed,outOf))).check(matches(isDisplayed()));
    }

    private static String xOutOfY(int x, int y){
        return Integer.toString(x) +"/"+Integer.toString(y);
    }

//    private void navToCapture() {
//        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
//        onView(withText(R.string.gallery_button_alt)).perform(click());
//        onView(withText(R.string.start_scan_exam)).perform(click());
//    }


}