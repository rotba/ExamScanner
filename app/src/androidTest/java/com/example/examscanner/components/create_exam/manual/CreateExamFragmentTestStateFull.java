package com.example.examscanner.components.create_exam.manual;

import com.example.examscanner.components.create_exam.CreateExamFragmentAbstractTestStateFull;
import com.example.examscanner.components.create_exam.get_version_file.VersionImageGetterFactory;
import com.example.examscanner.image_processing.ImageProcessingFactory;

import org.junit.Test;

import static com.example.examscanner.ImageProcessorsGenerator.fakeIP;

public class CreateExamFragmentTestStateFull extends CreateExamFragmentAbstractTestStateFull {
    @Test
    public void testOnCreatedExamItAddsToTheHomeAdapterStubIP() {
        VersionImageGetterFactory.setStubInstance(null);
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(fakeIP());
        testOnCreatedExamItAddsToTheHomeAdapter();
    }
}
