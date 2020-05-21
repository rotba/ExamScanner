package com.example.examscanner;





import com.example.examscanner.components.create_exam.unit.CreateExamFragmentTest;
import com.example.examscanner.components.create_exam.view_model.unit.CreateExamModelViewTest;
import com.example.examscanner.components.home.HomeFragmentTest;
import com.example.examscanner.components.scan_exam.capture_and_detect_corners.CaptureAndDetectCornersIntegrationManualTest;
import com.example.examscanner.components.scan_exam.capture_and_detect_corners.unit.CaptureAndDetectCornersIntegrationTest;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModelTest3;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// Runs all unit tests.
@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
//                CreateExamFragmentTest.class,
//                CornerDetectionViewModelTest3.class,
        }
)
public class CustumTestSuite {
}