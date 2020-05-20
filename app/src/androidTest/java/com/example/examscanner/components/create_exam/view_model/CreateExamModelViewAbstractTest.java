package com.example.examscanner.components.create_exam.view_model;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.authentication.state.StateHolder;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.components.create_exam.CreateExamModelView;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.exam.Version;
import com.example.examscanner.repositories.grader.GraderRepoFactory;
import com.example.examscanner.stubs.ImageProcessorStub;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public abstract class CreateExamModelViewAbstractTest {

    private CreateExamModelView out;
    private Repository<Exam> examRepository;
    private ImageProcessorStub imageProcessor;

    @Before
    public void setUp() throws Exception {
        AppDatabaseFactory.setTestMode();
        TestObserver to = new TestObserver(){
            @Override
            public void onNext(Object value) {
                StateFactory.get().login(StateHolder.getDefaultHolder(), value);
            }
        };
        AuthenticationHandlerFactory.getTest().authenticate().subscribe(to);
        to.awaitCount(1);
        imageProcessor = new ImageProcessorStub();
        out  = new CreateExamModelView(
                new ExamRepositoryFactory().create(),
                new GraderRepoFactory().create(),
                imageProcessor,
                StateFactory.get(),
                0
        );
        examRepository = new ExamRepositoryFactory().create();
    }

    @After
    public void tearDown() throws Exception {
        AppDatabaseFactory.tearDownDb();
        RemoteDatabaseFacadeFactory.tearDown();
        ExamRepositoryFactory.tearDown();
        CommunicationFacadeFactory.tearDown();
        GraderRepoFactory.tearDown();
    }


    public void testAddVersion() {
        final int[] expectedNumOfAnswers = new int[1];
        imageProcessor.scanAnswers(null, new ScanAnswersConsumer() {
            @Override
            public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                expectedNumOfAnswers[0] = numOfAnswersDetected;
                for (int i = 0; i <selections.length ; i++) {
                    if (selections[i]==-1)
                        expectedNumOfAnswers[0]--;
                }
            }
        });
        out.holdVersionBitmap(BitmapsInstancesFactoryAndroidTest.getTestJpg1());
        out.holdVersionNumber(3);
        out.holdGraderUsername("bob");
        out.addGrader();
        out.addVersion();
        out.create("testAddVersion()_courseName","A","Fall","2020");
        List<Exam> exams = examRepository.get((e)->true);
        assertEquals(exams.size(),1);
        Exam theExam = exams.get(0);
        assertTrue(theExam.getVersions().size()==1);
        final Version version = theExam.getVersions().get(0);
        assertEquals(expectedNumOfAnswers[0] ,version.getQuestions().size());
    }
}