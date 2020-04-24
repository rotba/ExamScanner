package com.example.examscanner.components.scan_exam.capture;


import android.os.Bundle;
import android.os.RemoteException;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.capture.camera.CameraMangerFactory;
import com.example.examscanner.components.scan_exam.detect_corners.DCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.example.examscanner.ImageProcessorsGenerator.fakeIP;
import static com.example.examscanner.ImageProcessorsGenerator.nullIP;
import static com.example.examscanner.Utils.sleepCameraPreviewSetupTime;
import static com.example.examscanner.Utils.sleepSingleCaptureProcessingTime;
import static com.example.examscanner.Utils.sleepSingleCaptureTakingTime;
import static com.example.examscanner.components.scan_exam.capture.CaptureUtils.assertUserSeeProgress;

@RunWith(AndroidJUnit4.class)
public class CaptureFragmentTest{
    private FragmentScenario<CaptureFragment> scenario;
    private UiDevice device;

    @Before
    public void setUp() {
        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
        CameraMangerFactory.setStubInstance(new CameraManagerStub());
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(fakeIP());
        Bundle b = new Bundle();
        b.putInt("examId", 0);
        b.putInt("sessionId", 0);
        scenario =FragmentScenario.launchInContainer(CaptureFragment.class, b);
        sleepCameraPreviewSetupTime();
    }

    private void theNumberOfUnprocessedCapturesUpdates() {
        onView(withId(R.id.capture_image_button)).perform(click());
        sleepSingleCaptureTakingTime();
        try {
            assertUserSeeProgress(0,1);
        }catch (NoMatchingViewException e){
            assertUserSeeProgress(1,1);
        }
    }

    @Test
    public void testTheNumberOfUnprocessedCapturesUpdates() {
        theNumberOfUnprocessedCapturesUpdates();
    }
    @Test
    public void testTheNumberOfUnprocessedCapturesUpdatesRealCamera() {
        CameraMangerFactory.setStubInstance(null);
        theNumberOfUnprocessedCapturesUpdates();
    }

    @Test
    public void testTheNumberOfUnprocessedCapturesUpdatesRealIP() {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(null);
        theNumberOfUnprocessedCapturesUpdates();
    }

    @Test
    public void testTheNumberOfProcessedAndUnprocessedCapturesUpdates() {
        sleepCameraPreviewSetupTime();
        onView(withId(R.id.capture_image_button)).perform(click());
        sleepSingleCaptureProcessingTime();
        assertUserSeeProgress(1,1);
    }

    @Test
    public void testTheNumberOfProcessedAndUnprocessedCapturesUpdatesRealCamera() {
        CameraMangerFactory.setStubInstance(null);
        sleepCameraPreviewSetupTime();
        onView(withId(R.id.capture_image_button)).perform(click());
        sleepSingleCaptureProcessingTime();
        assertUserSeeProgress(1,1);
    }

    @Test
    @Ignore("TODO - fix")
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


}