package com.example.examscanner.components.scan_exam.detect_corners;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import com.example.examscanner.MainActivity;
import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.Utils;
import com.example.examscanner.image_processing.ICornerDetectionResult;
import com.example.examscanner.image_processing.IScannedCapture;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCaptureRepositoryFacrory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

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
        repo.create(convert(imageProcessor.detectCorners(null)));
        repo.create(convert(imageProcessor.detectCorners(null)));
        FragmentScenario.launchInContainer(CornerDetectionFragment.class);
        onView(withText("1/2")).check(matches(isDisplayed()));
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeLeft());
        onView(withText("2/2")).check(matches(isDisplayed()));
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeRight());
        onView(withText("1/2")).check(matches(isDisplayed()));
    }

    private CornerDetectedCapture convert(ICornerDetectionResult icr){
        return new CornerDetectedCapture(icr.getBitmap());
    }


}