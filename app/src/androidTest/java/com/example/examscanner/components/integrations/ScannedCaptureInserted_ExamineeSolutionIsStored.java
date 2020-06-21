package com.example.examscanner.components.integrations;

import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.authentication.state.StateHolder;
import com.example.examscanner.components.scan_exam.AbstractComponentInstrumentedTest;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacade;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.persistence.remote.entities.ExamineeSolution;
import com.example.examscanner.repositories.scanned_capture.Answer;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext3Setuper;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ScannedCaptureInserted_ExamineeSolutionIsStored extends AbstractComponentInstrumentedTest {
    private CornerDetectionContext3Setuper usecaseContext;
    private RemoteDatabaseFacade out;


    @Override
    @Before
    public void setUp() {
        super.setUp();
        TestObserver to = new TestObserver(){
            @Override
            public void onNext(Object value) {
                StateFactory.get().login(StateHolder.getDefaultHolder(), value);
            }
        };
        AuthenticationHandlerFactory.getTest().authenticate().subscribe(to);
        usecaseContext = new CornerDetectionContext3Setuper();
        usecaseContext.setup();
        out = RemoteDatabaseFacadeFactory.get();
    }

    @Test
    public void theSolutionIsStoredInTheDB() {
        List<ExamineeSolution> es = new ArrayList<>();
        out.getExamineeSolutions().blockingSubscribe(_es->es.addAll(_es));
        ExamineeSolution actual = null;
        final ScannedCapture expected = usecaseContext.getSc();
        for (ExamineeSolution currEs:es) {
            if(currEs._getId().equals(expected.getExamineeId())){
                actual = currEs;
            }
        }
        assertNotNull(actual);
        assertTrue(actual.answers.size() == expected.getAnswers().size());
        for (Answer a : expected.getAnswers()) {
            Integer actualAns = actual.answers.get(String.valueOf(a.getAnsNum()));
            Integer expectedAns = new Integer(a.getSelection());
            assertEquals(actualAns , expectedAns);
        }
    }
}
