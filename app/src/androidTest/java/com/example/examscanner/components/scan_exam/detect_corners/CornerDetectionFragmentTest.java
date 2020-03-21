package com.example.examscanner.components.scan_exam.detect_corners;

import android.graphics.Point;
import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.action.ViewActions;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactory;
import com.example.examscanner.components.scan_exam.reslove_answers.SCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCaptureRepositoryFacrory;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.ImageProcessorsGenerator.nullIP;
import static com.example.examscanner.ImageProcessorsGenerator.slowIP;
import static com.example.examscanner.Utils.sleepRectangleTransformationTime;
import static com.example.examscanner.Utils.sleepScanAnswersTime;
import static org.hamcrest.Matchers.not;

public class CornerDetectionFragmentTest {
    private  Repository<CornerDetectedCapture> repo;
    private ImageProcessingFacade imageProcessor;

    @Before
    public void setUp() {
        CornerDetectedCaptureRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(SCEmptyRepositoryFactory.create());
        imageProcessor = nullIP();
        repo = new CornerDetectedCaptureRepositoryFacrory().create();
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(slowIP());
    }
    @After
    public void tearDown(){
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(null);
        CornerDetectedCaptureRepositoryFacrory.ONLYFORTESTINGsetTestInstance(null);
    }

    @Test
    public void testCorrentPositionPointer() {
        DetectCornersConsumer consumer = new DetectCornersConsumer() {
            @Override
            public void consume(Point upperLeft, Point upperRight, Point bottomLeft, Point bottomRight) {
                repo.create(new CornerDetectedCapture(repo.genId(),null,upperLeft, upperRight, bottomLeft, bottomRight));
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

    @Test
    public void testProcessingFeedback() {
        DetectCornersConsumer consumer = new DetectCornersConsumer() {
            @Override
            public void consume(Point upperLeft, Point upperRight, Point bottomLeft, Point bottomRight) {
                repo.create(new CornerDetectedCapture(repo.genId(),null,upperLeft, upperRight, bottomLeft, bottomRight));
            }
        };
        imageProcessor.detectCorners(null, consumer);
        imageProcessor.detectCorners(null, consumer);
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        onView(withId(R.id.textView_cd_processing_progress)).check(matches(withText("0/2")));
        onView(withId(R.id.button_approve_and_scan_answers)).perform(click());
        sleepRectangleTransformationTime();
        sleepScanAnswersTime();
        onView(withId(R.id.textView_cd_processing_progress)).check(matches(withText("1/2")));
    }

    @Test
    public void testProgressIndicator() {
        DetectCornersConsumer consumer = new DetectCornersConsumer() {
            @Override
            public void consume(Point upperLeft, Point upperRight, Point bottomLeft, Point bottomRight) {
                repo.create(new CornerDetectedCapture(repo.genId(),BitmapsInstancesFactory.getTestJpg1(),upperLeft, upperRight, bottomLeft, bottomRight));
            }
        };
        imageProcessor.detectCorners(BitmapsInstancesFactory.getTestJpg1(), consumer);
        imageProcessor.detectCorners(BitmapsInstancesFactory.getTestJpg1(), consumer);
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        onView(withId(R.id.button_approve_and_scan_answers)).perform(click());
        onView(withId(R.id.progressBar2_scanning_answers)).check(matches(isDisplayed()));
        sleepRectangleTransformationTime();
        sleepScanAnswersTime();
        onView(withId(R.id.progressBar2_scanning_answers)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testOnFinishProcessFragmentIsDiscarded2Captures() {
        DetectCornersConsumer consumer = new DetectCornersConsumer() {
            @Override
            public void consume(Point upperLeft, Point upperRight, Point bottomLeft, Point bottomRight) {
                repo.create(new CornerDetectedCapture(repo.genId(),BitmapsInstancesFactory.getTestJpg1(),upperLeft, upperRight, bottomLeft, bottomRight));
            }
        };
        imageProcessor.detectCorners(BitmapsInstancesFactory.getTestJpg1(), consumer);
        imageProcessor.detectCorners(BitmapsInstancesFactory.getTestJpg2(), consumer);
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        onView(withId(R.id.button_approve_and_scan_answers)).perform(click());
        sleepRectangleTransformationTime();
        sleepScanAnswersTime();
        onView(withId(R.id.textView_cd_current_position)).check(matches(withText("1/1")));
    }
    @Test
    public void testOnFinishProcessFragmentIsDiscarded3Captures() {;
        imageProcessor.detectCorners(BitmapsInstancesFactory.getTestJpg1(), new DetectCornersConsumer() {
            @Override
            public void consume(Point upperLeft, Point upperRight, Point bottomLeft, Point bottomRight) {
                repo.create(new CornerDetectedCapture(repo.genId(),BitmapsInstancesFactory.getTestJpg1(),upperLeft, upperRight, bottomLeft, bottomRight));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactory.getTestJpg2(), new DetectCornersConsumer() {
            @Override
            public void consume(Point upperLeft, Point upperRight, Point bottomLeft, Point bottomRight) {
                repo.create(new CornerDetectedCapture(repo.genId(),BitmapsInstancesFactory.getTestJpg2(),upperLeft, upperRight, bottomLeft, bottomRight));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactory.getTestJpg3(), new DetectCornersConsumer() {
            @Override
            public void consume(Point upperLeft, Point upperRight, Point bottomLeft, Point bottomRight) {
                repo.create(new CornerDetectedCapture(repo.genId(),BitmapsInstancesFactory.getTestJpg3(),upperLeft, upperRight, bottomLeft, bottomRight));
            }
        });
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        onView(withId(R.id.button_approve_and_scan_answers)).perform(click());
        sleepRectangleTransformationTime();
        sleepScanAnswersTime();
        onView(withId(R.id.textView_cd_current_position)).check(matches(withText("1/2")));
    }
}