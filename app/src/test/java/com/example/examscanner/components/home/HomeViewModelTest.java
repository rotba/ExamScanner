package com.example.examscanner.components.home;

import com.example.examscanner.AbstractTestSuite;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.repositories.exam.Exam;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HomeViewModelTest extends AbstractTestSuite {
    private HomeViewModel oot;

    @Before
    @Override
    public void setUp(){
        super.setUp();
        oot = new HomeViewModel();
        oot.init();
    }

    @Test
    public void testGraderGetsOnlyHisTests(){
        for (Exam e: oot.getExams().getValue()) {
            assertTrue(e.associatedWithGrader(getCurrentGraderId()));
        }
    }
}