package com.example.examscanner.components.create_exam;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.testing.FragmentScenario;

import com.example.examscanner.R;
import com.example.examscanner.Utils;
import com.example.examscanner.components.create_exam.get_version_file.VersionImageGetter;
import com.example.examscanner.components.create_exam.get_version_file.VersionImageGetterFactory;
import com.example.examscanner.components.scan_exam.AbstractComponentInstrumentedTest;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;

import org.junit.Test;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.Utils.loadOpenCV;
import static org.hamcrest.Matchers.containsString;

public class CreateExamFragmentTest extends AbstractComponentInstrumentedTest {

    private FragmentScenario<CreateExamFragment> scenraio;

    @Override
    public void setUp() {
        super.setUp();
        VersionImageGetterFactory.setStubInstance(new VersionImageGetter() {
            @Override
            public void get(FragmentActivity activity) {}

            @Override
            public Bitmap accessBitmap(Intent data, FragmentActivity activity) {
                return BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked();
            }
        });
        scenraio = FragmentScenario.launchInContainer(CreateExamFragment.class);
        loadOpenCV(scenraio);
    }


    @Test
    public void testAddVersion() {
        onView(withId(R.id.button_create_exam_upload_version_image)).perform(click());
        scenraio.onFragment(f ->
                f.onActivityResult(0,0,null)
        );
        onView(withId(R.id.textView_number_of_versions_added)).check(matches(withText("1")));
    }
}
