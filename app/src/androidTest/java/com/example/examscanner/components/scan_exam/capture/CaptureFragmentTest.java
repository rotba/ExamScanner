package com.example.examscanner.components.scan_exam.capture;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.examscanner.AbstractComponentInstrumentedTest;
import com.example.examscanner.MainActivity;
import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.ScanExamActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class CaptureFragmentTest extends AbstractComponentInstrumentedTest {
//    @Rule
//    public ActivityScenarioRule<MainActivity> mainActivityScenarioRule =
//            new ActivityScenarioRule<MainActivity>(MainActivity.class);
//    @Rule
//    public ActivityScenarioRule<ScanExamActivity> scanActivityScenarioRule =
//            new ActivityScenarioRule<ScanExamActivity>(ScanExamActivity.class);

    @Rule
    public IntentsTestRule<ScanExamActivity> intentsRule =
            new IntentsTestRule<>(ScanExamActivity.class);


    @Test
    public void testTheNumberOfUnprocessedCapturesUpdates() {
        assertTheUserSeesThatTheNumberOfUnprocessedCapturesIs(0);
        onView(withId(R.id.capture_image_button)).perform(click());
        assertTheUserSeesThatTheNumberOfUnprocessedCapturesIs(1);
    }

    private void assertTheUserSeesThatTheNumberOfUnprocessedCapturesIs(int i) {
        onView(withId(R.id.unprocessed_captures_text)).check(matches(withText(Integer.toString(i))));
    }
}