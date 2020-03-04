package com.example.examscanner.components.home;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.example.examscanner.AbstractComponentInstrumentedTest;
import com.example.examscanner.MainActivity;
import com.example.examscanner.R;
import com.example.examscanner.State;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.Factory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.List;
import java.util.function.Predicate;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


public class MyAdapterInstrumentedTest extends AbstractComponentInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);



    @Test
    public void testTheGraderSeesOnlyHisExams() {
        navigateToHome();
        for (Exam e: getCurrentGraderExams()) {
            onView(withText(e.toString())).check(matches(isDisplayed()));
        }
    }

    private void navigateToHome() {
        onView(ViewMatchers.withId(R.id.bLogin))
                .perform(ViewActions.click());
    }

    private List<Exam> getCurrentGraderExams() {
        return new Factory().create().get(new Predicate<Exam>() {
            @Override
            public boolean test(Exam exam) {
                return exam.associatedWithGrader(getCurrentGraderId());
            }
        });
    }




}