package com.example.examscanner.components.scan_exam.detect_corners;

import android.graphics.Bitmap;

import com.example.examscanner.components.scan_exam.AbstractComponentInstrumentedTest;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.repositories.scanned_capture.Answer;
import com.example.examscanner.repositories.scanned_capture.ResolvedAnswer;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.stubs.RemoteDatabaseStubInstance;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CornerDetectionViewModelTest3 extends CornerDetectionViewModelTest2 {

    private interface SetUpCallback{CornerDetectionContext2Setuper createContext();}
    private SetUpCallback setupCallback;
    private CornerDetectionViewModel out;


    @Before
    public void setUp() {}

    public void lateSetup() {
        super.setUp();
    }


    @NotNull
    @Override
    protected CornerDetectionContext2Setuper getUseCaseContext() {
        return setupCallback.createContext();
    }

    @Test
    public void scanAnswersByPositionsSanity() {
        setupCallback = ()->super.getUseCaseContext();
        lateSetup();
        super.scanAnswersByPositions();
    }

    @Test
    public void scanAnswersByPositionsInsatnce1() {
        setupCallback = ()->new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_1());
        lateSetup();
        super.scanAnswersByPositions();
    }
}
