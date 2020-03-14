package com.example.examscanner.components.scan_exam.reslove_answers;

import androidx.fragment.app.testing.FragmentScenario;

import com.example.examscanner.R;
import com.example.examscanner.StateFullTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class ResolveAnswersFragmentTest extends StateFullTest {

    @Before
    @Override
    public void setUp() {
        super.setUp();
        navToResolveAnswers();
    }

    @Test
    public void testCheck50IdentifiedAnd10Unidentified() {
        onView(withText("Identified: 50")).check(matches(isDisplayed()));
        onView(withText("Unidentified: 10")).check(matches(isDisplayed()));
    }

    private void navToResolveAnswers() {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withText(R.string.gallery_button_alt)).perform(click());
        onView(withText(R.string.resolve_answers)).perform(click());
        System.out.println("asd");
    }
}