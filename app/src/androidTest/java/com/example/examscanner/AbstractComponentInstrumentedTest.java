package com.example.examscanner;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.persistence.local.AppDatabase;
import com.example.examscanner.persistence.local.AppDatabaseFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public abstract class AbstractComponentInstrumentedTest {
    protected AppDatabase db;
    protected DBCallback dbCallback = (theDb -> {
    });
    protected Runnable setupCallback = ()->{};


    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityScenarioRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() {
        AppDatabaseFactory.setTestMode();
        db = AppDatabaseFactory.getInstance();
        dbCallback.call(db);
        setupCallback.run();
    }

    @After
    public void tearDown() throws Exception {
        AppDatabaseFactory.tearDownDb();
    }

    protected interface DBCallback {
        public void call(AppDatabase db);
    }
}
