package com.example.examscanner;


import com.example.examscanner.components.scan_exam.CaptureAndDetectCornersIntegrationTest;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModelTest3;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// Runs all unit tests.
@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                CaptureAndDetectCornersIntegrationTest.class,
                CornerDetectionViewModelTest3.class,
        }
)
public class CustumTestSuite {
}