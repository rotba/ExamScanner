package com.example.examscanner.components.scan_exam.capture;


import android.os.Bundle;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import com.example.examscanner.AbstractComponentInstrumentedTest;
import com.example.examscanner.MainActivity;
import com.example.examscanner.R;
import com.example.examscanner.State;
import com.example.examscanner.StateFullTest;
import com.example.examscanner.components.scan_exam.detect_corners.EmptyRepositoryFactory;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCaptureRepositoryFacrory;

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.example.examscanner.components.scan_exam.Utils.navFromHomeToCapture;
import static com.example.examscanner.components.scan_exam.Utils.sleepCameraPreviewSetupTime;
import static com.example.examscanner.components.scan_exam.Utils.sleepSingleCaptureProcessingTime;
import static com.example.examscanner.components.scan_exam.Utils.sleepSingleCaptureTakingTime;
import static com.example.examscanner.components.scan_exam.capture.CaptureUtils.assertUserSeeProgress;

@RunWith(AndroidJUnit4.class)
public class CaptureFragmentTest{

    private FragmentScenario<CaptureFragment> scenario;
    private UiDevice device;

    @Before
    public void setUp() {
//        super.setUp();
//        navFromHomeToCapture();
        CornerDetectedCaptureRepositoryFacrory.ONLYFORTESTINGsetTestInstance(EmptyRepositoryFactory.create());
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        scenario =
                FragmentScenario.launchInContainer(CaptureFragment.class, b);
        sleepCameraPreviewSetupTime();
    }

//    @After
//    public void tearDown(){
////        device = UiDevice.getInstance(getInstrumentation());
//        try {
//            device.setOrientationNatural();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    public void testTheNumberOfUnprocessedCapturesUpdates() {
        assertUserSeeProgress(0,0);
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

    @Test//TODO - fix
    @Ignore
    public void testDataSurvivesRotation() {
        device = UiDevice.getInstance(getInstrumentation());
        sleepCameraPreviewSetupTime();
        onView(withId(R.id.capture_image_button)).perform(click());
        sleepSingleCaptureProcessingTime();
        try {
            device.setOrientationLeft();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            device.setOrientationNatural();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        assertUserSeeProgress(1, 1);
    }





//    private void navToCapture() {
//        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
//        onView(withText(R.string.gallery_button_alt)).perform(click());
//        onView(withText(R.string.start_scan_exam)).perform(click());
//    }


}