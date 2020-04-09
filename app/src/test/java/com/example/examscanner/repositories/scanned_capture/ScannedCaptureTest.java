package com.example.examscanner.repositories.scanned_capture;

import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.stubs.ScannedCapturesInstancesFactory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScannedCaptureTest {
    private ScannedCapture out;

    @Before
    public void setUp() throws Exception {
        ScannedCapturesInstancesFactory.instance1(
                new ScanAnswersConsumer() {
                    @Override
                    public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                        out  = new ScannedCapture(0, null,answersIds.length,numOfAnswersDetected,answersIds,lefts,tops,rights,bottoms,selections);
                    }
                }
        );

    }

    @Test
    public void sanityChecks() {
        assertTrue(out.getAnswerByNum(3).isResolved());
        assertFalse(out.getAnswerByNum(3).isConflicted());
        assertFalse(out.getAnswerByNum(3).isMissing());
        assertTrue(out.getAnswerByNum(10).isConflicted());
        assertFalse(out.getAnswerByNum(10).isResolved());
        assertFalse(out.getAnswerByNum(10).isMissing());
        assertTrue(out.getAnswerByNum(2).isMissing());
        assertFalse(out.getAnswerByNum(2).isConflicted());
        assertFalse(out.getAnswerByNum(2).isResolved());
    }
}