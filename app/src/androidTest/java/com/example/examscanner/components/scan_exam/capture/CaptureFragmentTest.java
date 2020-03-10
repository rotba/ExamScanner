package com.example.examscanner.components.scan_exam.capture;


import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.AbstractComponentInstrumentedTest;
import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.ScanExamActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CaptureFragmentTest extends AbstractComponentInstrumentedTest {
    @Rule
    public ActivityScenarioRule<ScanExamActivity> scanExamActivityActivityScenarioRule =
            new ActivityScenarioRule<ScanExamActivity>(ScanExamActivity.class);






    @Test
    public void testTheNumberOfUnprocessedCapturesUpdates() {

        assertUserSeeProgress(0,0);
        onView(withId(R.id.capture_image_button)).perform(click());
        assertUserSeeProgress(0,1);
    }

    private void assertUserSeeProgress(int processed, int outOf) {
        onView(withText(xOutOfY(processed,outOf))).check(matches(isDisplayed()));
    }

    private static String xOutOfY(int x, int y){
        return Integer.toString(x) +"/"+Integer.toString(y);
    }
}