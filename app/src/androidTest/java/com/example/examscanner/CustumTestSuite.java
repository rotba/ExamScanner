package com.example.examscanner;


import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// Runs all unit tests.
@RunWith(Suite.class)
@Suite.SuiteClasses(
        {

//                com.example.examscanner.MainActivityTest.class,
//                com.example.examscanner.communication.CommunicationFacadeTest.class,
//                com.example.examscanner.components.create_exam.intergration_with_ip.CreateExamFragmentTest.class,
//                com.example.examscanner.components.create_exam.intergration_with_ip.CreateExamFragmentTestStateFull.class,
//                com.example.examscanner.components.create_exam.manual.CreateExamFragmentTestStateFull.class,
//                com.example.examscanner.components.create_exam.unit.CreateExamFragmentTest.class,
//                com.example.examscanner.components.create_exam.unit.CreateExamFragmentTestStateFull.class,
//                com.example.examscanner.components.create_exam.view_model.integration_with_remote_db.CreateExamModelViewTest.class,
//                com.example.examscanner.components.create_exam.view_model.integration_with_remote_db.CreateExamUpdatesGraderTest.class,
//                com.example.examscanner.components.create_exam.view_model.unit.CreateExamModelViewTest.class,
//                com.example.examscanner.components.home.HomeFragmentTest.class,
//                com.example.examscanner.components.scan_exam.capture.CaptureFragmentTest.class,
//                com.example.examscanner.components.scan_exam.capture_and_detect_corners.CaptureAndDetectCornersIntegrationManualTest.class,
//                com.example.examscanner.components.scan_exam.capture_and_detect_corners.unit.CaptureAndDetectCornersIntegrationTest.class,
//                com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionFragmentTest.class,
//                com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModelTest.class,
//                com.example.examscanner.components.scan_exam.detect_corners_and_resolve_ans.ip_integration.DetectCornersAndResolveAnswersTest.class,
//                com.example.examscanner.components.scan_exam.detect_corners_and_resolve_ans.unit.DetectCornersAndResolveAnswersTest.class,
//                com.example.examscanner.components.scan_exam.reslove_answers.ResolveAnswersANdResolveConflictsTest.class,
//                com.example.examscanner.components.scan_exam.reslove_answers.ResolveAnswersFragmentTest.class,
//                com.example.examscanner.components.scan_exam.reslove_answers.ResolveAnswersViewModelTest.class,
//                com.example.examscanner.components.scan_exam.reslove_answers.resolve_conflicted_answers.ResolveConflictedAnswersFragmentTest.class,
                com.example.examscanner.core_algorithm.CoreAlgorithmJPGTest.class,
                com.example.examscanner.core_algorithm.CoreAlgorithmPDFTest.class,
                com.example.examscanner.image_processing.ImageProcessorTest.class,
                com.example.examscanner.persistence.local.daos.ScanExamScanExammSessionDaoTest.class,
                com.example.examscanner.persistence.local.daos.SemiScannedCaptureDaoTest.class,
                com.example.examscanner.persistence.local.entities.QuestionExamineeSolutionCrossResTest.class,
                com.example.examscanner.repositories.exam.ExamIsCreatedTest.class,
                com.example.examscanner.repositories.exam.ExamRepositoryTest.class
        }
)

public class CustumTestSuite {
}
