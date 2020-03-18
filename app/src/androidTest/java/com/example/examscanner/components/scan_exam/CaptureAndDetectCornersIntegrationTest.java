package com.example.examscanner.components.scan_exam;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnitRunner;

import com.example.examscanner.R;
import com.example.examscanner.StateFullTest;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.components.scan_exam.Utils.TEMP_TESTImageProcessor;
import static com.example.examscanner.components.scan_exam.Utils.sleepCameraPreviewSetupTime;
import static com.example.examscanner.components.scan_exam.Utils.sleepMovingFromCaptureToDetectCorners;
import static com.example.examscanner.components.scan_exam.Utils.sleepSingleCaptureProcessingTime;

@RunWith(AndroidJUnit4.class)
public class CaptureAndDetectCornersIntegrationTest extends StateFullTest {

    private ImageProcessingFacade imageProcessor;

//    @Before
//    @Override
//    public void setUp() {
//        super.setUp();
//        imageProcessor = TEMP_TESTImageProcessor();
//    }

    @Test
    public void testWhenTheGraderStartCornerDetectionHeSeesHowManyCapturesThereAreANdWhereIsHe() {
        navToCapture();
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

    private void navToCapture() {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withText(R.string.gallery_button_alt)).perform(click());
        onView(withText(R.string.start_scan_exam)).perform(click());
    }
}
