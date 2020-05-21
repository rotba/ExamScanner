package com.example.examscanner.components.scan_exam.capture_and_detect_corners.unit;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.components.scan_exam.capture_and_detect_corners.CaptureAndDetectCornersIntegrationAbsractTest;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.stubs.RemoteDatabaseStubInstance;
import com.example.examscanner.use_case_contexts_creators.Context2Setuper;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CaptureAndDetectCornersIntegrationTest extends CaptureAndDetectCornersIntegrationAbsractTest {

    private String test_course_name;

    @Before
    @Override
    public void setUp() {
        RemoteDatabaseFacadeFactory.setStubInstance(new RemoteDatabaseStubInstance());
        super.setUp();
    }

    @NotNull
    @Override
    protected Context2Setuper getContext() {
        return new Context2Setuper();
    }
}
