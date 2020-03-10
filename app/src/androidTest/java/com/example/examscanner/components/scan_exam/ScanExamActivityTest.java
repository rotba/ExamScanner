package com.example.examscanner.components.scan_exam;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.examscanner.R;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class ScanExamActivityTest {
    @Rule
    public ActivityScenarioRule<ScanExamActivity> scanExamActivityScenarioRule =
            new ActivityScenarioRule<ScanExamActivity>(ScanExamActivity.class);

    @Test
    public void testCaptureFragementIsDisplayed() {
        onView(withId(R.id.preview_view)).check(matches(isDisplayed()));
    }
}