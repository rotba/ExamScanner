package com.example.examscanner.core_algorithm;

import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModel;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;

public class CoreAlgorithmJPGTest extends CoreAlgorithmAbstractTest {

    private interface SetUpCallback{CornerDetectionContext2Setuper createContext();}
    private SetUpCallback setupCallback;
    private CornerDetectionViewModel out;


    @Before
    public void setUp() {}

    public void lateSetup() {
//        TestObserver<FirebaseAuth> observer = new TestObserver<FirebaseAuth>(){
//            @Override
//            public void onNext(FirebaseAuth firebaseAuth) {
////                currentUserId = firebaseAuth.getUid();
//            }
//        };
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
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_1());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce2() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_2());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce3() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_3());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce4() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_4());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce5() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_5());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce6() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_6());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce7() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_7());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce8() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_8());
        lateSetup();
        super.scanAnswersByPositions();
    }

    @Test
    public void scanAnswersByPositionsInsatnce9() {
        setupCallback = ()->new CornerDetectionContext2Setuper(
                getState(),
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_9(),
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_in1()
        );
        lateSetup();
        super.scanAnswersByPositions();
    }

}
