package com.example.examscanner;


import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionFragmentTest;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModelTest;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModelTest2;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModelTest3;
import com.example.examscanner.components.scan_exam.detect_corners_and_resolve_ans.DetectCornersAndResolveAnswersAbstractTest;
import com.example.examscanner.components.scan_exam.detect_corners_and_resolve_ans.ip_integration.DetectCornersAndResolveAnswersTest;
import com.example.examscanner.components.scan_exam.reslove_answers.ResolveAnswersANdResolveConflictsTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// Runs all unit tests.
@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
//                CornerDetectionFragmentTest.class,
//                CornerDetectionViewModelTest2.class,
//                CornerDetectionViewModelTest3.class
        }
)
public class CustumTestSuite {
}