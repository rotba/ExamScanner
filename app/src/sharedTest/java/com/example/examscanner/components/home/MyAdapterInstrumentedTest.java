package com.example.examscanner.components.home;


import com.example.examscanner.StateFullTest;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;

import org.junit.Test;


import java.util.List;
import java.util.function.Predicate;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


public class MyAdapterInstrumentedTest extends StateFullTest {

    @Test
    public void testTheGraderSeesOnlyHisExams() {
        for (Exam e: getCurrentGraderExams()) {
            onView(withText(e.toString())).check(matches(isDisplayed()));
        }
    }

    private List<Exam> getCurrentGraderExams() {
        return new ExamRepositoryFactory().create().get(new Predicate<Exam>() {
            @Override
            public boolean test(Exam exam) {
                return exam.associatedWithGrader(getCurrentGraderId());
            }
        });
    }




}