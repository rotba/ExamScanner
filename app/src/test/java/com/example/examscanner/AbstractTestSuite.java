package com.example.examscanner;



import com.example.examscanner.communication.CommunicationFacadeFactory;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
public abstract class AbstractTestSuite {
    @Before
    public void setUp(){
        CommunicationFacadeFactory.setTestMode();
    }
    public int getCurrentGraderId(){
        return State.getState().getGraderId();
    }
}
