package com.example.examscanner.components.scan_exam;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.communication.RealFacadeImple;
import com.example.examscanner.persistence.AppDatabase;

import org.junit.After;
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
    private AppDatabase db;
    @Before
    public void setUp(){
        CommunicationFacadeFactory.setTestMode();
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase.class).build();
        RealFacadeImple.setDBTestInstance(db);
    }

    @After
    public void tearDown() throws Exception {
        db.clearAllTables();
    }
}
