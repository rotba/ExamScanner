package com.example.examscanner.components.scan_exam;

import com.example.examscanner.R;
import com.example.examscanner.StateFullTest;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.components.scan_exam.Utils.sleepCameraPreviewSetupTime;
import static com.example.examscanner.components.scan_exam.Utils.sleepMovingFromCaptureToDetectCorners;
import static com.example.examscanner.components.scan_exam.Utils.sleepSingleCaptureProcessingTime;

public class CaptureAndDetectCornersIntegrationTest {
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
    private void navToCapture() {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withText(R.string.gallery_button_alt)).perform(click());
        onView(withText(R.string.start_scan_exam)).perform(click());
    }
}
