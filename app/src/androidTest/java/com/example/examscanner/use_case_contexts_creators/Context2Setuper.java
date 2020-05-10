package com.example.examscanner.use_case_contexts_creators;

import com.example.examscanner.components.scan_exam.capture.CameraManagerStub;
import com.example.examscanner.components.scan_exam.capture.camera.CameraMangerFactory;
import com.example.examscanner.components.scan_exam.detect_corners.DCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;

import static com.example.examscanner.ImageProcessorsGenerator.fakeIP;

public class Context2Setuper {

    public void setup(){
        CameraMangerFactory.setStubInstance(new CameraManagerStub());
        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(fakeIP());
    }
}
