package com.example.examscanner.components.scan_exam;

import android.graphics.Point;

import com.example.examscanner.R;
import com.example.examscanner.StateFullTest;
import com.example.examscanner.Utils;
import com.example.examscanner.components.scan_exam.detect_corners.DCEmptyRepositoryFactory;
import com.example.examscanner.components.scan_exam.reslove_answers.SCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCaptureRepositoryFacrory;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.ImageProcessorsGenerator.nullIP;

public class DetectCornersAndResolveAnswersTest extends StateFullTest {
    private Repository<CornerDetectedCapture> repo;
    private ImageProcessingFacade imageProcessor;


    @Before
    @Override
    public void setUp() {
        super.setUp();
        CornerDetectedCaptureRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(SCEmptyRepositoryFactory.create());
        imageProcessor = nullIP();
        repo = new CornerDetectedCaptureRepositoryFacrory().create();
        // Create a TestNavHostController
        imageProcessor.detectCorners(BitmapsInstancesFactory.getTestJpg1(), new DetectCornersConsumer() {
            @Override
            public void consume(Point upperLeft, Point upperRight, Point bottomLeft, Point bottomRight) {
                repo.create(new CornerDetectedCapture(repo.genId(), BitmapsInstancesFactory.getTestJpg1(), upperLeft, upperRight, bottomLeft, bottomRight));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactory.getTestJpg2(), new DetectCornersConsumer() {
            @Override
            public void consume(Point upperLeft, Point upperRight, Point bottomLeft, Point bottomRight) {
                repo.create(new CornerDetectedCapture(repo.genId(), BitmapsInstancesFactory.getTestJpg2(), upperLeft, upperRight, bottomLeft, bottomRight));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactory.getTestJpg3(), new DetectCornersConsumer() {
            @Override
            public void consume(Point upperLeft, Point upperRight, Point bottomLeft, Point bottomRight) {
                repo.create(new CornerDetectedCapture(repo.genId(), BitmapsInstancesFactory.getTestJpg3(), upperLeft, upperRight, bottomLeft, bottomRight));
            }
        });
        Utils.navFromHomeToDetecteCorners();
    }

    @Test
    public void testProcessedCornerDetectedCapturesConsistentBetweenFragments() {
        Utils.sleepCDFragmentSetupTime();
        onView(withId(R.id.button_approve_and_scan_answers)).perform(click());
        onView(withId(R.id.button_approve_and_scan_answers)).perform(click());
        onView(withId(R.id.button_nav_to_resolve_answers)).perform(click());
        onView(withId(R.id.textView_ra_progress_feedback)).check(matches(withText("1/2")));
    }

    @Test
    public void testProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionProgress() {
        Utils.sleepCDFragmentSetupTime();
        onView(withId(R.id.button_approve_and_scan_answers)).perform(click());
        onView(withId(R.id.button_approve_and_scan_answers)).perform(click());
        onView(withId(R.id.button_nav_to_resolve_answers)).perform(click());
        onView(withContentDescription("Navigate up")).perform(click());
        onView(withId(R.id.textView_cd_processing_progress)).check(matches(withText("2/3")));
    }

    @Test
    public void testProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPosition() {
        Utils.sleepCDFragmentSetupTime();
        onView(withId(R.id.button_approve_and_scan_answers)).perform(click());
        onView(withId(R.id.button_approve_and_scan_answers)).perform(click());
        onView(withId(R.id.button_nav_to_resolve_answers)).perform(click());
        onView(withContentDescription("Navigate up")).perform(click());
        onView(withId(R.id.textView_cd_current_position)).check(matches(withText("1/1")));
    }

}