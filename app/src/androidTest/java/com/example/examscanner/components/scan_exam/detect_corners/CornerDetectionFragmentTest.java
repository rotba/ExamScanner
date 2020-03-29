package com.example.examscanner.components.scan_exam.detect_corners;

import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.action.ViewActions;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
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
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

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
import static com.example.examscanner.Utils.sleepSwipingTime;
import static org.hamcrest.Matchers.not;

public class CornerDetectionFragmentTest {
    private static final String TAG = "CornerDetectionFragmentTest";
    private  Repository<CornerDetectedCapture> repo;
    private ImageProcessingFacade imageProcessor;
    private BaseLoaderCallback mLoaderCallback;
    private long sessionId;

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

    private void setUp5Captures(){
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1(),upperLeft, upperRight, bottomLeft, bottomRight,null,sessionId));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg2(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg2(),upperLeft, upperRight, bottomLeft, bottomRight,null,sessionId));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg3(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg3(),upperLeft, upperRight, bottomLeft, bottomRight,null,sessionId));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg3(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg3(),upperLeft, upperRight, bottomLeft, bottomRight,null,sessionId));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg3(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg3(),upperLeft, upperRight, bottomLeft, bottomRight,null,sessionId));
            }
        });
    }

    private void setUp3MarkedCaptures() {
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(),upperLeft, upperRight, bottomLeft, bottomRight,null,sessionId));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg2Marked(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg2Marked(),upperLeft, upperRight, bottomLeft, bottomRight,null,sessionId));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg3Marked(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg3Marked(),upperLeft, upperRight, bottomLeft, bottomRight,null,sessionId));
            }
        });

    }

    private void resolveAnswersAndSwipeLeft() {
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
//        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeLeft());
        sleepSwipingTime();
    }

    @Test
    public void testProcess5Captures(){
        setUp5Captures();
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(null);
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        FragmentScenario<CornerDetectionFragment> scenraio =  FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        loadOpenCV(scenraio);
        resolveAnswersAndSwipeLeft();
        resolveAnswersAndSwipeLeft();
        resolveAnswersAndSwipeLeft();
        resolveAnswersAndSwipeLeft();
    }

    @Test
    public void testProgressBarStays(){
        setUp3MarkedCaptures();
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(slowIP());
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        FragmentScenario<CornerDetectionFragment> scenraio =  FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        loadOpenCV(scenraio);
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeLeft());
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
    public void testAutoLeftSwipeOnProcess(){
        setUp3MarkedCaptures();
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(slowIP());
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        FragmentScenario<CornerDetectionFragment> scenraio =  FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        loadOpenCV(scenraio);
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
        sleepSwipingTime();
        onView(withId(R.id.textView_cd_current_position)).check(matches(withText("2/3")));
    }

    @Test
    public void testCorrentPositionPointer() {
        DetectCornersConsumer consumer = new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(null,upperLeft, upperRight, bottomLeft, bottomRight,null,sessionId));
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
    public void testProcessingFeedbackSlowIP(){
        testProcessingFeedback(slowIP());
    }
    @Test
    public void testProcessingFeedbackRealIP(){
        testProcessingFeedback(null);
    }

    private void testProcessingFeedback(ImageProcessingFacade ip) {
        DetectCornersConsumer consumer = new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1(),upperLeft, upperRight, bottomLeft, bottomRight,null,sessionId));
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
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
        sleepRectangleTransformationTime();
        sleepScanAnswersTime();
        onView(withId(R.id.textView_cd_processing_progress)).check(matches(withText("1/2")));
    }

    @Test
    public void testProgressIndicator() {
        DetectCornersConsumer consumer = new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1(),upperLeft, upperRight, bottomLeft, bottomRight,null,sessionId));
            }
        };
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), consumer);
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), consumer);
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
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
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1(),upperLeft, upperRight, bottomLeft, bottomRight,null,sessionId));
            }
        };
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), consumer);
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg2(), consumer);
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(ip);
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        FragmentScenario<CornerDetectionFragment> s =  FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        loadOpenCV(s);
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
        sleepRectangleTransformationTime();
        sleepScanAnswersTime();
        onView(withId(R.id.textView_cd_current_position)).check(matches(withText("1/1")));
    }
    @Test
    public void testOnFinishProcessFragmentIsDiscarded3CapturesSlowIP(){
        testOnFinishProcessFragmentIsDiscarded3Captures(slowIP());
    }
    @Test
    public void testOnFinishProcessFragmentIsDiscarded3CapturesRealIP(){
        testOnFinishProcessFragmentIsDiscarded3Captures(null);
    }

    public void testOnFinishProcessFragmentIsDiscarded3Captures(ImageProcessingFacade ip) {;
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1(),upperLeft, upperRight, bottomLeft, bottomRight,null,sessionId));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg2(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg2(),upperLeft, upperRight, bottomLeft, bottomRight,null,sessionId));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg3(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg3(),upperLeft, upperRight, bottomLeft, bottomRight,null,sessionId));
            }
        });
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(ip);
        Bundle b = new Bundle();
        b.putInt("examId", -1);
        FragmentScenario<CornerDetectionFragment> scenraio =  FragmentScenario.launchInContainer(CornerDetectionFragment.class, b);
        loadOpenCV(scenraio);
        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
        sleepRectangleTransformationTime();
        sleepScanAnswersTime();
        onView(withId(R.id.textView_cd_current_position)).check(matches(withText("1/2")));
    }

    private void loadOpenCV(FragmentScenario<CornerDetectionFragment> scenraio) {
        scenraio.onFragment(fragment -> {
            mLoaderCallback = new BaseLoaderCallback(fragment.getActivity()) {
                @Override
                public void onManagerConnected(int status) {
                    switch (status) {
                        case LoaderCallbackInterface.SUCCESS: {
                            Log.i("MainActivity", "OpenCV loaded successfully");
                        }
                        break;
                        default: {
                            super.onManagerConnected(status);
                        }
                        break;
                    }
                }
            };
        });

        scenraio.onFragment(fragment -> {
            if (!OpenCVLoader.initDebug()) {
                Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
                OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, fragment.getActivity(), mLoaderCallback);
            } else {
                Log.d(TAG, "OpenCV library found inside package. Using it!");
                mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            }
        });
    }


}