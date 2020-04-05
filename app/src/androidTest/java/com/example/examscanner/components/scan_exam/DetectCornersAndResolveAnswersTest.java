package com.example.examscanner.components.scan_exam;

import android.graphics.PointF;

import com.example.examscanner.R;
import com.example.examscanner.Utils;
import com.example.examscanner.components.scan_exam.detect_corners.DCEmptyRepositoryFactory;
import com.example.examscanner.components.scan_exam.reslove_answers.SCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;

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
import static com.example.examscanner.ImageProcessorsGenerator.nullIP;
import static com.example.examscanner.ImageProcessorsGenerator.slowIP;


public class DetectCornersAndResolveAnswersTest extends StateFullTest {
    private Repository<CornerDetectedCapture> repo;
    private ImageProcessingFacade imageProcessor;
    private long session;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(SCEmptyRepositoryFactory.create());
        imageProcessor = nullIP();
        repo = new CDCRepositoryFacrory().create();
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), upperLeft, bottomRight, session));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg2(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg2Marked(), upperLeft, bottomRight, session));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg3(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg3Marked(), upperLeft, bottomRight, session));
            }
        });
        Utils.navFromHomeToDetecteCorners();
    }

    @Test
    public void testProcessedCornerDetectedCapturesConsistentBetweenFragments() {
        Utils.sleepCDFragmentSetupTime();
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(swipeLeft());
        Utils.sleepSwipingTime();
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
        Utils.sleepScanAnswersTime();
        Utils.sleepScanAnswersTime();
        onView(withId(R.id.button_cd_nav_to_resolve_answers)).perform(click());
        onView(withId(R.id.textView_ra_progress_feedback)).check(matches(withText("1/2")));
    }

    @Test
    public void testProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionProgress() {
        Utils.sleepCDFragmentSetupTime();
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(swipeLeft());
        Utils.sleepSwipingTime();
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
        Utils.sleepSwipingTime();
        Utils.sleepScanAnswersTime();
        Utils.sleepScanAnswersTime();
        onView(withId(R.id.button_cd_nav_to_resolve_answers)).perform(click());
        Utils.sleepSwipingTime();
        onView(withContentDescription("Navigate up")).perform(click());
        onView(withId(R.id.textView_cd_processing_progress)).check(matches(withText("2/3")));
    }


    @Test
    @Ignore
    public void testProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPosition() {
        Utils.sleepCDFragmentSetupTime();
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(swipeLeft());
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
        onView(withId(R.id.button_cd_nav_to_resolve_answers)).perform(click());
        onView(withContentDescription("Navigate up")).perform(click());
        Utils.sleepScanAnswersTime();
        Utils.sleepScanAnswersTime();
        Utils.sleepScanAnswersTime();
        onView(withId(R.id.textView_cd_current_position)).check(matches(withText("1/1")));
    }

    @Test
    public void testNotCrashProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPositionSlowIP() {
        testNotCrashProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPosition(slowIP());
    }

    @Test
    public void testNotCrashProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPositionRealIP() {
        testNotCrashProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPosition(null);
    }

    private void testNotCrashProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPosition(ImageProcessingFacade ip) {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(ip);
        Utils.sleepCDFragmentSetupTime();
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(swipeLeft());
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
        onView(withId(R.id.button_cd_nav_to_resolve_answers)).perform(click());
        onView(withContentDescription("Navigate up")).perform(click());
        Utils.sleepScanAnswersTime();
        Utils.sleepScanAnswersTime();
        Utils.sleepScanAnswersTime();
        onView(withId(R.id.textView_cd_current_position)).check(matches(withText("1/1")));
    }

}