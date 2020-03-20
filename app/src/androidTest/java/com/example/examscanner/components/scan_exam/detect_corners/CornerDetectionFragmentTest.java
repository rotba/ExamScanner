package com.example.examscanner.components.scan_exam.detect_corners;

import android.graphics.Point;
import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import com.example.examscanner.MainActivity;
import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.Utils;
import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCaptureRepositoryFacrory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class CornerDetectionFragmentTest {
    private  Repository<CornerDetectedCapture> repo;
    private ImageProcessingFacade imageProcessor;
    @Rule
    public ActivityTestRule<MainActivity> mainActivityScenarioRule=
            new ActivityTestRule<MainActivity>(MainActivity.class);
    @Before
    public void setUp() {
        CornerDetectedCaptureRepositoryFacrory.ONLYFORTESTINGsetTestInstance(EmptyRepositoryFactory.create());
        imageProcessor = Utils.TEMP_TESTImageProcessor();
        repo = new CornerDetectedCaptureRepositoryFacrory().create();
    }

    @Test
    public void testCorrentPositionPointer() {
        DetectCornersConsumer consumer = new DetectCornersConsumer() {
            @Override
            public void consume(Point upperLeft, Point upperRight, Point bottomLeft, Point bottomRight) {
                repo.create(new CornerDetectedCapture(null,upperLeft, upperRight, bottomLeft, bottomRight));
            }
        };
        imageProcessor.detectCorners(null, consumer);
        imageProcessor.detectCorners(null, consumer);
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        onView(withText("1/2")).check(matches(isDisplayed()));
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeLeft());
        onView(withText("2/2")).check(matches(isDisplayed()));
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeRight());
        onView(withText("1/2")).check(matches(isDisplayed()));
    }


}