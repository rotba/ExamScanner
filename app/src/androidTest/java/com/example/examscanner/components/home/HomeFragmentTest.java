package com.example.examscanner.components.home;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.StateFullTest;
import com.example.examscanner.persistence.entities.Exam;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class HomeFragmentTest extends StateFullTest {

    private String comp;
    private String caspl;

    @Override
    @Before
    public void setUp() {
        dbCallback = db -> {
            comp = "COMP";
            db.getExamDao().insert(new Exam(comp, 0,"2020","THE_EMPTY_URL",0,0));
            caspl = "CASPL";
            db.getExamDao().insert(new Exam(caspl, 1,"2020","THE_EMPTY_URL",0,0));
        };
        super.setUp();
    }

    @Test
    public void onItemClick() {
        onView(withText(containsString(comp))).perform(click());
        onView(withId(R.layout.fragment_capture)).check(matches(isDisplayed()));
    }
}