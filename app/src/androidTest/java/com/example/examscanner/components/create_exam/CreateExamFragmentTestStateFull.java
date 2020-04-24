package com.example.examscanner.components.create_exam;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.examscanner.R;
import com.example.examscanner.Utils;
import com.example.examscanner.components.create_exam.get_version_file.VersionImageGetter;
import com.example.examscanner.components.create_exam.get_version_file.VersionImageGetterFactory;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.StateFullTest;
import com.example.examscanner.image_processing.ImageProcessingFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.ImageProcessorsGenerator.fakeIP;
import static org.hamcrest.Matchers.containsString;

public class CreateExamFragmentTestStateFull extends StateFullTest {

    Fragment f;
    @Override
    @Before
    public void setUp() {
        super.setUp();
        VersionImageGetterFactory.setStubInstance(new VersionImageGetter() {
            @Override
            public void get(Fragment fragment, int pickfileRequestCode) {
                f = fragment;
            }

            @Override
            public Bitmap accessBitmap(Intent data, FragmentActivity activity) {
                return BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked();
            }
        });
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
    private void testCreateOneExam(String comp, String year, String verNum, String term, String aGraderAdress) {
        onView(withId(R.id.editText_create_exam_course_name)).perform(replaceText(comp));
        onView(withId(R.id.radioButton_create_exam_term_b)).perform(click());
        onView(withId(R.id.radioButton_create_exam_semester_fall)).perform(click());
        onView(withId(R.id.editText_create_exam_year)).perform(replaceText(year));
        onView(withId(R.id.editText_create_exam_version_number)).perform(replaceText(verNum));
        onView(withId(R.id.button_create_exam_upload_version_image)).perform(click());
        mainActivityScenarioRule.getScenario().onActivity(a->f.onActivityResult(0,0,null));
        onView(withId(R.id.button_create_exam_add_version)).perform(click());
        Utils.sleepScanAnswersTime();
        onView(withId(R.id.editText_create_exam_grader_address)).perform(replaceText(aGraderAdress));
        onView(withId(R.id.textView_number_of_versions_added)).check(matches(withText("1")));
        onView(withId(R.id.button_create_exam_upload_version_image)).perform(click());
        onView(withId(R.id.editText_create_exam_version_number)).perform(replaceText("20"));
        onView(withId(R.id.button_create_exam_add_version)).perform(click());


        onView(withId(R.id.button_create_exam_create)).perform(click());
        Utils.sleepAlertPoppingTime();
        onView(withText(R.string.create_exam_dialog_ok)).perform(click());
        onView(withText(containsString(comp))).check(matches(isDisplayed()));
        onView(withText(containsString(year))).check(matches(isDisplayed()));
    }

    private void testCreateCourse(String courseName, String year, String term) {
        final String verNum = "3";
        final String aGraderAdress = "rotba@post.bgu.ac.il";
        testCreateOneExam(courseName, year, verNum, term, aGraderAdress);
    }

    private void testOnCreatedExamItAddsToTheHomeAdapter() {
        onView(withId(R.id.floating_button_home_create_exam)).perform(click());
        testCreateCourse("COMP", "2020", "1");
    }

    private void testOn2CreatedExamItAddsToTheHomeAdapter() {
        onView(withId(R.id.floating_button_home_create_exam)).perform(click());
        testCreateCourse("COMP", "2020", "1");
        onView(withId(R.id.floating_button_home_create_exam)).perform(click());
        testCreateCourse("SPL", "2019", "2");
    }

    @Test
    public void testOnCreatedExamItAddsToTheHomeAdapterStubIP() {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(fakeIP());
        testOnCreatedExamItAddsToTheHomeAdapter();
    }
    @Test
    public void testOnCreatedExamItAddsToTheHomeAdapterRealIP() {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(null);
        testOnCreatedExamItAddsToTheHomeAdapter();
    }

    @Test
    public void testOn2CreatedExamItAddsToTheHomeAdapterStubIP() {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(fakeIP());
        testOn2CreatedExamItAddsToTheHomeAdapter();
    }

    @Test
    public void testOn2CreatedExamItAddsToTheHomeAdapterRealIP() {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(null);
        testOn2CreatedExamItAddsToTheHomeAdapter();
    }
}