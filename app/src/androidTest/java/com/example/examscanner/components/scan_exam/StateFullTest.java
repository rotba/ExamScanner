package com.example.examscanner.components.scan_exam;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.examscanner.R;

import org.junit.After;
import org.junit.Before;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isFocusable;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


public abstract class StateFullTest extends AbstractComponentInstrumentedTest {
    private static final String TAG = "StateFullTest";

    @Before
    @Override
    public void setUp() {
        super.setUp();
        login();
    }



    private void login() {
        try {
            onView(withText("Sign in with email")).perform(click());
            onView(withHint("Email")).perform(replaceText("examscanner80@gmail.com"));
            onView(withText("NEXT")).perform(click());
            onView(withHint("Password")).perform(replaceText("Ycombinator"));
            onView(withText("SIGN IN")).perform(click());
        }catch (NoMatchingViewException e){
            onView(withText("Exam Scanner")).perform(click());
        }
    }

//    @After
//    public void tearDown() throws Exception {
//        super.tearDown();
//    }
}
