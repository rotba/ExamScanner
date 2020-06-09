package com.example.examscanner.core_algorithm;

import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.communication.RealFacadeImple;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModel;
import com.example.examscanner.persistence.local.files_management.FilesManagerFactory;
import com.example.examscanner.stubs.FilesManagerStub;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoreAlgorithmJPGTest extends CoreAlgorithmAbstractTest {

    private interface SetUpCallback{CornerDetectionContext2Setuper createContext();}
    private SetUpCallback setupCallback;
    private CornerDetectionViewModel out;



    @Before
    public void setUp() {
        FilesManagerFactory.setStubInstance(new FilesManagerStub());
    }

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
    @Test
    public void scanAnswersByPositionsInsatnce2() {
        setupCallback = ()->new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_2());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce3() {
        setupCallback = ()->new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_3());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce4() {
        setupCallback = ()->new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_4());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce5() {
        setupCallback = ()->new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_5());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce6() {
        setupCallback = ()->new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_6());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce7() {
        setupCallback = ()->new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_7());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce8() {
        setupCallback = ()->new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_8());
        lateSetup();
        super.scanAnswersByPositions();
    }

    @Test
    public void scanAnswersByPositionsInsatnce9() {
        setupCallback = ()->new CornerDetectionContext2Setuper(
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_9(),
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_in1()
        );
        lateSetup();
        super.scanAnswersByPositions();
    }

}
