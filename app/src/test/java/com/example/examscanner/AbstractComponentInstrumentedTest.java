package com.example.examscanner;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.communication.CommunicationFacadeFactory;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public abstract class AbstractComponentInstrumentedTest {
    @Before
    public void setUp(){
        CommunicationFacadeFactory.setTestMode();
    }
}
