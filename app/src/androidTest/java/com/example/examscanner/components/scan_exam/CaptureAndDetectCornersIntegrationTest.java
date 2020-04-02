package com.example.examscanner.components.scan_exam;


import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.capture.CameraManagerStub;
import com.example.examscanner.components.scan_exam.capture.camera.CameraManager;
import com.example.examscanner.components.scan_exam.capture.camera.CameraMangerFactory;
import com.example.examscanner.components.scan_exam.capture.camera.CameraOutputHander;
import com.example.examscanner.components.scan_exam.detect_corners.DCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.ImageProcessorsGenerator.quickIP;
import static com.example.examscanner.Utils.sleepCameraPreviewSetupTime;
import static com.example.examscanner.Utils.sleepMovingFromCaptureToDetectCorners;
import static com.example.examscanner.Utils.sleepSingleCaptureProcessingTime;
import static com.example.examscanner.Utils.sleepSwipingTime;

@RunWith(AndroidJUnit4.class)
public class CaptureAndDetectCornersIntegrationTest extends StateFullTest {

    private ImageProcessingFacade imageProcessor;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        CameraMangerFactory.setStubInstance(new CameraManagerStub());
        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
    }

    @Test
    public void testWhenTheGraderStartCornerDetectionHeSeesHowManyCapturesThereAreANdWhereIsHe() {
        navToCapture();
        sleepCameraPreviewSetupTime();
        sleepCameraPreviewSetupTime();
        int numOfCaptures = 2;
        for (int i = 0; i <numOfCaptures ; i++) {
            onView(withId(R.id.capture_image_button)).perform(click());
            sleepSingleCaptureProcessingTime();
        }
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepMovingFromCaptureToDetectCorners();
        onView(withText("1/2")).check(matches(isDisplayed()));
    }

    @Test
    public void testTheAppStateStaysUpdatedWhenNavigatingForthAndBackBetweenCornerDetAndCapture() {
        navToCapture();
        sleepCameraPreviewSetupTime();
        int numOfCaptures = 2;
        for (int i = 0; i <numOfCaptures ; i++) {
            onView(withId(R.id.capture_image_button)).perform(click());
            sleepSingleCaptureProcessingTime();
        }
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepMovingFromCaptureToDetectCorners();
        onView(withContentDescription("Navigate up")).perform(click());
        sleepCameraPreviewSetupTime();
        onView(withText("2/2")).check(matches(isDisplayed()));
    }

    @Test
    public void testTheAppStateStaysUpdatedWhenNavigatingForthAndBacAndForthkBetweenCornerDetAndCapture() {
        navToCapture();
        sleepCameraPreviewSetupTime();
        int numOfCaptures = 2;
        for (int i = 0; i <numOfCaptures ; i++) {
            onView(withId(R.id.capture_image_button)).perform(click());
            sleepSingleCaptureProcessingTime();
        }
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepMovingFromCaptureToDetectCorners();
        onView(withContentDescription("Navigate up")).perform(click());
        sleepCameraPreviewSetupTime();
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepMovingFromCaptureToDetectCorners();
        onView(withText("1/2")).check(matches(isDisplayed()));
    }

    @Test
    public void testProcessedCornerDetectedCapturesNotLeaking() {
        navToCapture();
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(quickIP());
        sleepCameraPreviewSetupTime();
        onView(withId(R.id.capture_image_button)).perform(click());
        sleepSingleCaptureProcessingTime();
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepMovingFromCaptureToDetectCorners();
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
        sleepSwipingTime();
        sleepSwipingTime();
        onView(withContentDescription("Navigate up")).perform(click());
        onView(withId(R.id.capture_image_button)).perform(click());
        sleepSwipingTime();
        sleepSwipingTime();
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepMovingFromCaptureToDetectCorners();
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
        sleepSwipingTime();
        sleepSwipingTime();
    }

    @Test
    public void testWhenTheGraderStartCornerDetectionHeSeesHowManyCapturesThereAreANdWhereIsHeNoRepoStub() {
        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(null);
        testWhenTheGraderStartCornerDetectionHeSeesHowManyCapturesThereAreANdWhereIsHe();
    }

    @Test
    public void testWhenTheGraderStartCornerDetectionHeSeesHowManyCapturesThereAreANdWhereIsHeNoRepoStubNoCamStub() {
        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(null);
        CameraMangerFactory.setStubInstance(null);
        testWhenTheGraderStartCornerDetectionHeSeesHowManyCapturesThereAreANdWhereIsHe();
    }

    private void navToCapture() {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withText(R.string.gallery_button_alt)).perform(click());
        onView(withText(R.string.start_scan_exam)).perform(click());
    }
}
