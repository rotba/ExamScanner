package com.example.examscanner.components.scan_exam.detect_corners;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.examscanner.R;
import com.example.examscanner.StateFullTest;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.components.scan_exam.Utils.navFromCaptureToDetectCorners;
import static com.example.examscanner.components.scan_exam.Utils.navFromHomeToCapture;
import static com.example.examscanner.components.scan_exam.Utils.sleepSwipingTime;
import static com.example.examscanner.components.scan_exam.capture.CaptureUtils.takeTwoCaptures;
import static org.junit.Assert.*;

public class CornerDetectionFragmentTest extends StateFullTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testCorrentPositionPointer() {
        navFromHomeToCapture();
        takeTwoCaptures();
        navFromCaptureToDetectCorners();
        onView(withText("1/2")).check(matches(isDisplayed()));
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeLeft());
        onView(withText("2/2")).check(matches(isDisplayed()));
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeRight());
        onView(withText("1/2")).check(matches(isDisplayed()));
    }


}