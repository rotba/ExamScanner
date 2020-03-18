package com.example.examscanner.components.scan_exam.reslove_answers;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.R;
import com.example.examscanner.StateFullTest;
import com.example.examscanner.components.scan_exam.Utils;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionFragment;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCaptureRepositoryFacrory;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ResolveAnswersFragmentTest extends StateFullTest {
    private ImageProcessingFacade imageProcessor;
    private Repository<ScannedCapture> repo;
    @Before
    public void setUp() {
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(EmptyRepositoryFactory.create());
        imageProcessor = Utils.TEMP_TESTImageProcessor();
        repo = new ScannedCaptureRepositoryFactory().create();
    }
    @Test
    public void test40Idnetified10UnidentfiedVisible() {
        repo.create(new ScannedCapture(40,10,null));
        repo.create(new ScannedCapture(35,15,null));
        FragmentScenario.launchInContainer(ResolveAnswersFragment.class);
        onView(withText("Identified: 40")).check(matches(isDisplayed()));
        onView(withText("Unidentified: 10")).check(matches(isDisplayed()));
    }

    @Test
    public void testSwiping() {
        repo.create(new ScannedCapture(40,10,null));
        repo.create(new ScannedCapture(35,15,null));
        FragmentScenario.launchInContainer(ResolveAnswersFragment.class);
        onView(withText("1/2")).check(matches(isDisplayed()));
        onView(withId(R.id.viewPager2_scanned_captures)).perform(ViewActions.swipeLeft());
        onView(withText("2/2")).check(matches(isDisplayed()));
        onView(withId(R.id.viewPager2_scanned_captures)).perform(ViewActions.swipeRight());
        onView(withText("1/2")).check(matches(isDisplayed()));
    }
}