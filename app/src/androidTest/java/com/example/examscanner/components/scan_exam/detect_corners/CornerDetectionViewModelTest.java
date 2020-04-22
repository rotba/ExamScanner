package com.example.examscanner.components.scan_exam.detect_corners;

import android.graphics.PointF;

import com.example.examscanner.components.scan_exam.AbstractComponentInstrumentedTest;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.reslove_answers.SCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.persistence.entities.ExamCreationSession;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.exam.Version;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.repositories.session.ScanExamSessionProviderFactory;
import com.example.examscanner.stubs.ExamStubFactory;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext1Setuper;

import org.junit.Before;
import org.junit.Test;

import static com.example.examscanner.ImageProcessorsGenerator.nullIP;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CornerDetectionViewModelTest extends AbstractComponentInstrumentedTest {
    private CornerDetectionContext1Setuper useCaseContext;
    private CornerDetectionViewModel out;

    @Before
    public void setUp() {
        super.setUp();
        useCaseContext = new CornerDetectionContext1Setuper();
        useCaseContext.setup();
        out = new CornerDetectionViewModel(
                useCaseContext.getImageProcessor(),
                useCaseContext.getCDCRepo(),
                useCaseContext.getSCRepo(),
                useCaseContext.getTheExam()
        );
    }

    @Test
    public void getVersionNumbers() {
        assertArrayEquals(
                useCaseContext.getTheExam().getVersions().stream().mapToInt(Version::getNum).toArray(),
                out.getVersionNumbers()
        );
    }
}