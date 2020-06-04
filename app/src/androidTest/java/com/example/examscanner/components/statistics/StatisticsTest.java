package com.example.examscanner.components.statistics;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.examscanner.communication.CommunicationFacade;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.communication.StatisticsSourceStub;
import com.example.examscanner.stubs.AStatisticsSourceStub;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class StatisticsTest {
    protected Statistics out;
    private StatisticsSourceStub sss;

    @Before
    public void setUp() throws Exception {
        sss = new AStatisticsSourceStub();
        CommunicationFacadeFactory.setStatisticsProxy(sss);
    }

    @Test
    public void testAvgUpdates() throws InterruptedException {
        double[] observer = new double[1];
        double orig = observer[0];
        double someRandomeAvg = 70.123;
        out.getAvg().observe((LifecycleOwner) getInstrumentation().getContext(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                observer[0] = aDouble;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sss.setAvg(someRandomeAvg);
            }
        }).start();
        Thread.sleep(2000);
        assertTrue(someRandomeAvg==observer[0]);
    }

    @Test
    public void testVarUpdates() throws InterruptedException {
        double[] observer = new double[1];
        double orig = observer[0];
        double someRandomVariance = 10.123;
        out.getVar().observe((LifecycleOwner) getInstrumentation().getContext(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                observer[0] = aDouble;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sss.setVar(someRandomVariance);
            }
        }).start();
        Thread.sleep(2000);
        assertTrue(someRandomVariance==observer[0]);
    }


    @Test
    public void testNumOfSolutionsCheckedUpdates() throws InterruptedException {
        int[] observer = new int[1];
        int orig = observer[0];
        int someRandomNumOfSolutions = 10;
        out.getNumOfSolutions().observe((LifecycleOwner) getInstrumentation().getContext(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer anInteger) {
                observer[0] = anInteger;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sss.setNumOfSolutions(someRandomNumOfSolutions);
            }
        }).start();
        Thread.sleep(2000);
        assertTrue(someRandomNumOfSolutions==observer[0]);
    }
}