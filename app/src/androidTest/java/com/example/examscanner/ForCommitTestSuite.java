package com.example.examscanner;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                com.example.examscanner.components.scan_exam.detect_corners.ResolveConflictsTest.class,
                com.example.examscanner.components.scan_exam.capture.CaptureFragmentTest.class,
                com.example.examscanner.components.scan_exam.capture_and_detect_corners.unit.CaptureAndDetectCornersIntegrationTest.class,
                com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionFragmentTest.class,


        }
)

public class ForCommitTestSuite {
}
// Runs all unit tests.
