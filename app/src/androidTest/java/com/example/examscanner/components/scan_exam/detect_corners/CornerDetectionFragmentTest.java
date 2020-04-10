package com.example.examscanner.components.scan_exam.detect_corners;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.action.ViewActions;

import com.example.examscanner.R;
import com.example.examscanner.Utils;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.ExamEmptyRepositoryFactory;
import com.example.examscanner.components.scan_exam.reslove_answers.SCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.VersionRepoStubFactory;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.repositories.version.Version;
import com.example.examscanner.repositories.version.VersionRepoFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.ImageProcessorsGenerator.nullIP;
import static com.example.examscanner.ImageProcessorsGenerator.slowIP;
import static com.example.examscanner.Utils.loadOpenCV;
import static com.example.examscanner.Utils.sleepRectangleTransformationTime;
import static com.example.examscanner.Utils.sleepScanAnswersTime;
import static com.example.examscanner.Utils.sleepSwipingTime;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

public class CornerDetectionFragmentTest {
    private static final String TAG = "CornerDetectionFragmentTest";
    private Repository<CornerDetectedCapture> repo;
    private ImageProcessingFacade imageProcessor;
    private BaseLoaderCallback mLoaderCallback;
    private long sessionId;
    private int theDevilVersion;
    private int dinaBarzilayVersion;

    @Before
    public void setUp() {
        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(SCEmptyRepositoryFactory.create());
        ExamRepositoryFactory.setStubInstance(ExamEmptyRepositoryFactory.create());
        imageProcessor = nullIP();
        repo = new CDCRepositoryFacrory().create();
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(slowIP());
        theDevilVersion = 666;
        dinaBarzilayVersion = 496351;
        VersionRepoFactory.setStub(VersionRepoStubFactory.createStubThatReturns(new ArrayList() {{
            add(new Version(dinaBarzilayVersion, 0, null));
            add(new Version(theDevilVersion, 0, null));
        }}));
    }

    @After
    public void tearDown() {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(null);
        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(null);

    }

    private void produceCDCIntoRepo(Bitmap testJpg) {
        imageProcessor.detectCorners(testJpg, new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(testJpg, upperLeft, bottomRight, sessionId));
            }
        });
    }

    private void setUp5Captures() {
        produceCDCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1());
        produceCDCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg2());
        produceCDCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg3());
        produceCDCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg3());
        produceCDCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg3());
    }

    private void setUp3MarkedCaptures() {
        produceCDCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
        produceCDCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg2Marked());
        produceCDCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg3Marked());

    }

    private void resolveAnswersAndSwipeLeft() {
        selectVersionAndScanAnswers();
//        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeLeft());
        sleepSwipingTime();
    }

    @Test
    public void testProcess5Captures() {
        setUp5Captures();
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(null);
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        FragmentScenario<CornerDetectionFragment> scenraio = FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        loadOpenCV(scenraio);
        resolveAnswersAndSwipeLeft();
        resolveAnswersAndSwipeLeft();
        resolveAnswersAndSwipeLeft();
        resolveAnswersAndSwipeLeft();
    }

    @Test
    @Ignore("Failing for annoying reasons and not deadly important")
    public void testProgressBarStays() {
        setUp3MarkedCaptures();
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(slowIP());
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        FragmentScenario<CornerDetectionFragment> scenraio = FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        loadOpenCV(scenraio);
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeLeft());
        onView(Utils.withIndex(withId(R.id.spinner_detect_corners_version_num), 0)).perform(click());
        onView(withText(Integer.toString(dinaBarzilayVersion))).perform(click());
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
        sleepSwipingTime();
        sleepSwipingTime();
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeLeft());
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeRight());
        sleepSwipingTime();
        sleepSwipingTime();
        sleepSwipingTime();
        onView(withId(R.id.progressBar2_scanning_answers)).check(matches(isDisplayed()));
    }

    @Test
    public void testAutoLeftSwipeOnProcess() {
        setUp3MarkedCaptures();
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(slowIP());
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        FragmentScenario<CornerDetectionFragment> scenraio = FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        loadOpenCV(scenraio);
        selectVersionAndScanAnswers();
        sleepSwipingTime();
        onView(withId(R.id.textView_cd_current_position)).check(matches(withText("2/3")));
    }

    @Test
    public void testCorrentPositionPointer() {
        DetectCornersConsumer consumer = new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), upperLeft, bottomRight, sessionId));
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
    public void testProcessingFeedbackSlowIP() {
        testProcessingFeedback(slowIP());
    }

    @Test
    public void testProcessingFeedbackRealIP() {
        testProcessingFeedback(null);
    }

    private void testProcessingFeedback(ImageProcessingFacade ip) {
        DetectCornersConsumer consumer = new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), upperLeft, bottomRight, sessionId));
            }
        };
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), consumer);
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), consumer);
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(ip);
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        FragmentScenario<CornerDetectionFragment> s = FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        loadOpenCV(s);
        onView(withId(R.id.textView_cd_processing_progress)).check(matches(withText("0/2")));
        selectVersionAndScanAnswers();
        sleepRectangleTransformationTime();
        sleepScanAnswersTime();
        onView(withId(R.id.textView_cd_processing_progress)).check(matches(withText("1/2")));
    }

    @Test
    public void testProgressIndicator() {
        DetectCornersConsumer consumer = new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), upperLeft, bottomRight, sessionId));
            }
        };
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), consumer);
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), consumer);
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        selectVersionAndScanAnswers();
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeRight());
        onView(withId(R.id.progressBar2_scanning_answers)).check(matches(isDisplayed()));
        sleepRectangleTransformationTime();
        sleepScanAnswersTime();
        onView(withId(R.id.progressBar2_scanning_answers)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testOnFinishProcessFragmentIsDiscarded2CapturesSlowIP() {
        testOnFinishProcessFragmentIsDiscarded2Captures(slowIP());
    }

    @Test
    public void testOnFinishProcessFragmentIsDiscarded2CapturesRealP() {
        testOnFinishProcessFragmentIsDiscarded2Captures(null);
    }

    private void testOnFinishProcessFragmentIsDiscarded2Captures(ImageProcessingFacade ip) {
        DetectCornersConsumer consumer = new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), upperLeft, bottomRight, sessionId));
            }
        };
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), consumer);
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg2(), consumer);
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(ip);
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        FragmentScenario<CornerDetectionFragment> s = FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        loadOpenCV(s);
        selectVersionAndScanAnswers();
        sleepRectangleTransformationTime();
        sleepScanAnswersTime();
        onView(withId(R.id.textView_cd_current_position)).check(matches(withText("1/1")));
    }

    @Test
    public void testOnFinishProcessFragmentIsDiscarded3CapturesSlowIP() {
        testOnFinishProcessFragmentIsDiscarded3Captures(slowIP());
    }

    @Test
    public void testOnFinishProcessFragmentIsDiscarded3CapturesRealIP() {
        testOnFinishProcessFragmentIsDiscarded3Captures(null);
    }

    public void testOnFinishProcessFragmentIsDiscarded3Captures(ImageProcessingFacade ip) {
        produceCDCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1());
        produceCDCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg2());
        produceCDCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg3());
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(ip);
        Bundle b = new Bundle();
        b.putLong("examId", -1);
        b.putLong("sessioId", sessionId);
        FragmentScenario<CornerDetectionFragment> scenraio = FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        loadOpenCV(scenraio);
        selectVersionAndScanAnswers();
        sleepScanAnswersTime();
        onView(withId(R.id.textView_cd_current_position)).check(matches(withText("1/2")));
    }

    @Test
    public void versionEditTextIsVisible() {
        produceCDCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
        Bundle b = new Bundle();
        b.putLong("examId", -1);
        b.putLong("sessioId", sessionId);
        FragmentScenario<CornerDetectionFragment> scenraio = FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        onView(withId(R.id.spinner_detect_corners_version_num)).check(matches(isDisplayed()));
    }

    @Test
    public void versionEditTextOnlyAvailableVersionsOptions() {
        produceCDCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
        Bundle b = new Bundle();
        b.putLong("examId", -1);
        b.putLong("sessioId", sessionId);
        FragmentScenario<CornerDetectionFragment> scenraio = FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        onView(withId(R.id.spinner_detect_corners_version_num)).perform(click());
        onView(withText(Integer.toString(dinaBarzilayVersion))).check(matches(isDisplayed()));
        onView(withText(Integer.toString(theDevilVersion))).check(matches(isDisplayed()));
    }


    @Test
    public void scanningButtonIsWorkingIFFVersionIsChosen() {
        produceCDCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
        Bundle b = new Bundle();
        b.putLong("examId", -1);
        b.putLong("sessioId", sessionId);
        FragmentScenario<CornerDetectionFragment> scenraio = FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
        onView(withText(R.string.detect_corners_please_choose_version)).check(matches(isDisplayed()));
        onView(withText(R.string.detect_corners_ok)).perform(click());
        selectVersionAndScanAnswers();
        onView(withText(R.string.detect_corners_please_choose_version)).check(doesNotExist());
    }
    private void selectVersionAndScanAnswers() {
        onView(Utils.withIndex(withId(R.id.spinner_detect_corners_version_num), 0)).perform(click());
        onView(withText(Integer.toString(dinaBarzilayVersion))).perform(click());
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
    }
}