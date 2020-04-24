package com.example.examscanner.components.scan_exam;

import android.view.View;

import com.example.examscanner.R;
import com.example.examscanner.Utils;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext1Setuper;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.ImageProcessorsGenerator.fakeIP;
import static com.example.examscanner.ImageProcessorsGenerator.slowIP;
import static org.hamcrest.Matchers.containsString;


public class DetectCornersAndResolveAnswersStubIPTest extends DetectCornersAndResolveAnswersTest {

    @Override
    protected void navFromHomeToDetecteCornersUnderTestExam() {
        onView(withText(containsString(usecaseContext.getTheExam().getCourseName()))).perform(click());
        Utils.sleepAlertPoppingTime();
        onView(withText(R.string.home_dialog_yes)).perform().perform(click());
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(fakeIP());
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
    }

    @Override
    public void testProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPosition() {
        super.testProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPosition();
    }

    @Override
    public void testProcessedCornerDetectedCapturesConsistentBetweenFragments() {
        super.testProcessedCornerDetectedCapturesConsistentBetweenFragments();
    }

    @Override
    public void testProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionProgress() {
        super.testProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionProgress();
    }

    public void testNotCrashProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPositionSlowIP() {
        testNotCrashProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPosition(slowIP());
    }

    @Ignore
    @Override
    public void testNotCrashProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPositionRealIP() {
        super.testNotCrashProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPositionRealIP();
    }
}