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
import com.example.examscanner.StateFullTest;
import com.example.examscanner.repositories.grader.Grader;
import com.example.examscanner.repositories.grader.GraderRepoFactory;

import org.junit.After;
import org.junit.Before;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


public abstract class CreateExamFragmentAbstractTestStateFull extends StateFullTest {

    Fragment f;
    public static final String BOB_ID = "QR6JunUJDvaZr1kSOWEq3iiCToQ2";

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
        new GraderRepoFactory().create().create(new Grader("bob",BOB_ID));
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();

    }

    private void createExam(String comp, String year, String verNum, String aGraderAdress) {
        onView(withId(R.id.editText_create_exam_course_name)).perform(replaceText(comp));
        onView(withId(R.id.radioButton_create_exam_term_b)).perform(click());
        onView(withId(R.id.radioButton_create_exam_semester_fall)).perform(click());
        onView(withId(R.id.editText_create_exam_year)).perform(replaceText(year));
        onView(withId(R.id.editText_create_exam_version_number)).perform(replaceText(verNum));
        onView(withId(R.id.button_create_exam_upload_version_image)).perform(click());
        mainActivityScenarioRule.getScenario().onActivity(a -> f.onActivityResult(0, 0, null));
        onView(withId(R.id.button_create_exam_add_version)).perform(click());
        Utils.sleepScanAnswersTime();
        onView(withId(R.id.editText_create_exam_grader_address)).perform(replaceText(aGraderAdress));
        onView(withId(R.id.textView_number_of_versions_added)).check(matches(withText("1")));
        onView(withId(R.id.button_create_exam_upload_version_image)).perform(click());
        onView(withId(R.id.editText_create_exam_version_number)).perform(replaceText("20"));
        onView(withId(R.id.button_create_exam_add_version)).perform(click());
        onView(withId(R.id.button_create_exam_add_greader)).perform(click());
        Utils.sleepAlertPoppingTime();
        onView(withId(R.id.textView_create_exam_added_grader_feedback)).check(matches(withText("added bob")));
        onView(withId(R.id.button_create_exam_create)).perform(click());
        Utils.sleepAlertPoppingTime();
        onView(withText(R.string.create_exam_dialog_ok)).perform(click());
    }

    private void checkExamExists(String comp, String year) {
        onView(withText(containsString(comp))).check(matches(isDisplayed()));
        onView(withText(containsString(year))).check(matches(isDisplayed()));
    }

    private void testCreateOneExam(String comp, String year, String verNum, String term, String aGraderAdress) {
        createExam(comp, year, verNum, aGraderAdress);
        checkExamExists(comp, year);
    }

    private void testCreateCourse(String courseName, String year, String term) {
        final String verNum = "3";
        final String aGraderAdress = "bob";
        testCreateOneExam(courseName, year, verNum, term, aGraderAdress);
    }

    protected void testOnCreatedExamItAddsToTheHomeAdapter() {
        onView(withId(R.id.floating_button_home_create_exam)).perform(click());
        testCreateCourse("COMP", "2020", "1");
    }

    protected void testOn2CreatedExamItAddsToTheHomeAdapter() {
        onView(withId(R.id.floating_button_home_create_exam)).perform(click());
        testCreateCourse("COMP", "2020", "1");
        onView(withId(R.id.floating_button_home_create_exam)).perform(click());
        testCreateCourse("SPL", "2019", "2");
    }
}