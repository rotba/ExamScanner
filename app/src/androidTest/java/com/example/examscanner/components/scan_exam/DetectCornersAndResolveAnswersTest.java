package com.example.examscanner.components.scan_exam;

import com.example.examscanner.Utils;
import com.example.examscanner.components.scan_exam.detect_corners.DCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCaptureRepositoryFacrory;

import org.junit.Before;

import static com.example.examscanner.ImageProcessorsGenerator.nullIP;

public class DetectCornersAndResolveAnswersTest {
    private Repository<CornerDetectedCapture> repo;
    private ImageProcessingFacade imageProcessor;
    @Before
    public void setUp() {
        CornerDetectedCaptureRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
        imageProcessor = nullIP();
        repo = new CornerDetectedCaptureRepositoryFacrory().create();
    }
}
