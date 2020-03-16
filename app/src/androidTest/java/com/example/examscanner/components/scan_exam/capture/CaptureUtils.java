package com.example.examscanner.components.scan_exam.capture;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class CaptureUtils {
    public static void assertUserSeeProgress(int processed, int outOf) {
        onView(withText(xOutOfY(processed,outOf))).check(matches(isDisplayed()));
    }
    private static String xOutOfY(int x, int y){
        return Integer.toString(x) +"/"+Integer.toString(y);
    }
}
