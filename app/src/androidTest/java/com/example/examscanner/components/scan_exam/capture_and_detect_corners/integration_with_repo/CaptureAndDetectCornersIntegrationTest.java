package com.example.examscanner.components.scan_exam.capture_and_detect_corners.integration_with_repo;

import com.example.examscanner.components.scan_exam.capture_and_detect_corners.CaptureAndDetectCornersIntegrationAbsractTest;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class CaptureAndDetectCornersIntegrationTest extends CaptureAndDetectCornersIntegrationAbsractTest {
    @NotNull
    @Override
    protected CornerDetectionContext2Setuper getContext() {
        CornerDetectionContext2Setuper context2Setuper = new CornerDetectionContext2Setuper();
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(null);
        return context2Setuper;
    }

    @Test
    public void testRetake() {
        super.testRetake();
    }

    @Test
    public void testFinishBatch() {
        super.testFinishBatch();
    }

    @Test
    public void testFinishBatchAndToHome() {
        super.testFinishBatchAndToHome();
    }

    @Test
    public void testFinishBatchAndToCapture() {
        super.testFinishBatchAndToCapture();
    }
}
