package com.example.examscanner.components.scan_exam.detect_corners;

import com.example.examscanner.components.scan_exam.AbstractComponentInstrumentedTest;
import com.example.examscanner.repositories.exam.Version;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext1Setuper;

import org.junit.Before;
import org.junit.Test;

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