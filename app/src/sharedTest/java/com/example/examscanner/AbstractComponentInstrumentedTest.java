package com.example.examscanner;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.communication.CommunicationFacadeFactory;

import org.junit.Before;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public abstract class AbstractComponentInstrumentedTest {
    @Before
    public void setUp(){
        CommunicationFacadeFactory.setTestMode();
    }
}
