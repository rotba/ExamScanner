package com.example.examscanner.components.scan_exam.detect_corners;

import com.example.examscanner.components.scan_exam.AbstractComponentInstrumentedTest;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.Version;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.stubs.RemoteDatabaseStubInstance;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CornerDetectionViewModelTest2 extends AbstractComponentInstrumentedTest {

    private CornerDetectionContext2Setuper useCaseContext;
    private CornerDetectionViewModel out;

    @Before
    public void setUp() {
        RemoteDatabaseFacadeFactory.setStubInstance(new RemoteDatabaseStubInstance());
        super.setUp();
        useCaseContext = new CornerDetectionContext2Setuper();
        useCaseContext.setup();
        out = new CornerDetectionViewModel(
                useCaseContext.getImageProcessor(),
                useCaseContext.getCDCRepo(),
                useCaseContext.getSCRepo(),
                useCaseContext.getTheExam()
        );
    }

    @Override
    public void tearDown() throws Exception {
        useCaseContext.tearDown();
        super.tearDown();
    }

    @Test
    public void scanAnswersByPositions() {
        out.transformToRectangle(useCaseContext.getCDC());
        out.scanAnswers(useCaseContext.getCDC(), useCaseContext.getVersionNum());
        ArrayList<Integer> realAnswers = new ArrayList<Integer>(
                Arrays.asList(5, 4, 3, 3, 3, 3, 5, 2, 4, 1 ,1 , 1, 2 ,3 , 4, 5, 1, 2 ,3 , 4, 5, 1, 2 ,3 ,
                        4, 5, 1, 2 ,3 , 4, 5, 1, 2 ,3 , 4, 5, 1, 2 ,3 , 4, 5, 1, 2 ,3 , 4, 5, 1, 2 ,3 , 4, 5, 1, 2 ,3 , 4, 5, 1, 2 ,3 , 4, 5, 1, 2 ,3 , 4));
        List<ScannedCapture> sclst = useCaseContext.getSCRepo().get(x -> true);
        assert sclst.size() == 1;
        ScannedCapture sc = sclst.get(0);
        assertEquals(50 ,sc.getAnswers().size());
        for(int i =0 ; i < useCaseContext.getTheExam().getNumOfQuestions(); i++){
            assertTrue(realAnswers.get(i) == sc.getAnswerByNum(i+1).getAnsNum());
        }
    }
}
