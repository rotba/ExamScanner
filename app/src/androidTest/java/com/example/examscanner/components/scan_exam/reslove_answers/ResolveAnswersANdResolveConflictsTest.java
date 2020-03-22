package com.example.examscanner.components.scan_exam.reslove_answers;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;

import com.example.examscanner.R;
import com.example.examscanner.StateFullTest;
import com.example.examscanner.Utils;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.ImageProcessorsGenerator.nullIP;

public class ResolveAnswersANdResolveConflictsTest extends StateFullTest {
    private ImageProcessingFacade imageProcessor;
    private Repository<ScannedCapture> repo;
    private ResolveAnswersViewModel resolveAnswersViewModel;

    @Before
    public void setUp() {
        super.setUp();
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(SCEmptyRepositoryFactory.create());
        imageProcessor = nullIP();
        repo = new ScannedCaptureRepositoryFactory().create();
        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
        Utils.navFromHomeToResolveAnswers();
    }

    @Test
    public void testConflictAndCheckedAMountUpdatesUponResolution() {
        onView(withText("Resolve")).perform(click());
        resolveAndSwipe("4");
        Utils.sleepSwipingTime();
        resolveAndSwipe("5");
        Utils.sleepSwipingTime();
        resolveAndSwipe("No Answer");
        Utils.sleepSwipingTime();
        resolveAndSwipe("No Answer");
        Utils.sleepSwipingTime();
        onView(withContentDescription("Navigate up")).perform(click());
        onView(withText("Checked: 38")).check(matches(isDisplayed()));
        onView(withText("Conflicted: 0")).check(matches(isDisplayed()));
        onView(withText("Missing: 15")).check(matches(isDisplayed()));
    }

    private void resolveAndSwipe(String s) {
        onView(withText(s)).perform(click());

    }
}
