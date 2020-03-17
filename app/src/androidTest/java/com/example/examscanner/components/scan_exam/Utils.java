package com.example.examscanner.components.scan_exam;

import com.example.examscanner.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class Utils {
    public static void navFromHomeToCapture() {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withText(R.string.gallery_button_alt)).perform(click());
        onView(withText(R.string.start_scan_exam)).perform(click());
    }
    public static void navFromCaptureToDetectCorners(){
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
    }
    public static void sleepCameraPreviewSetupTime() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void sleepSwipingTime() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepSingleCaptureTakingTime() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void sleepSingleCaptureProcessingTime() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepMovingFromCaptureToDetectCorners() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepScreenRotationTime(){
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
