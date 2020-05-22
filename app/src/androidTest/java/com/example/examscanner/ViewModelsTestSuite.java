package com.example.examscanner;





import com.example.examscanner.components.create_exam.view_model.integration_with_remote_db.CreateExamModelViewTest;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModelTest;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModelTest3;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// Runs all unit tests.
@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
//                com.example.examscanner.components.create_exam.view_model.unit.CreateExamModelViewTest.class,
//                CreateExamModelViewTest.class,
//                CornerDetectionViewModelTest.class,
//                CornerDetectionViewModelTest3.class
        }
)
public class ViewModelsTestSuite {
}