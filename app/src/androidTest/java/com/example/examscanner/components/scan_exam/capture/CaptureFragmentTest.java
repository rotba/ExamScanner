package com.example.examscanner.components.scan_exam.capture;


import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.capture.camera.CameraManager;
import com.example.examscanner.components.scan_exam.capture.camera.CameraMangerFactory;
import com.example.examscanner.components.scan_exam.capture.camera.CameraOutputHander;
import com.example.examscanner.components.scan_exam.detect_corners.DCEmptyRepositoryFactory;
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
//        super.setUp();
//        navFromHomeToCapture();
        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
//        CameraMangerFactory.setStubInstance(new CameraManager() {
//            @Override
//            public void setUp() {}
//
//            @Override
//            public View.OnClickListener createCaptureClickListener(CameraOutputHander handler) {
//                return new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        handler.handleBitmap(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
//                    }
//                };
//            }
//
//            @Override
//            public void onPause() {}
//
//            @Override
//            public void onDestroy() {}
//        });
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