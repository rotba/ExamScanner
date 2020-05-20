package com.example.examscanner.components.create_exam.view_model.integration_with_remote_db;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.authentication.state.StateHolder;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.components.create_exam.CreateExamModelView;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.example.examscanner.persistence.remote.FirebaseDatabaseFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.grader.Grader;
import com.example.examscanner.repositories.grader.GraderRepoFactory;
import com.example.examscanner.stubs.ImageProcessorStub;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.observers.TestObserver;

import static com.example.examscanner.components.create_exam.CreateExamFragmentAbstractTestStateFull.BOB_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CreateExamUpdatesGraderTest {
    private CreateExamModelView out;
    private Repository<Exam> examRepository;
    private Repository<Grader> graderRepository;
    private ImageProcessorStub imageProcessor;
    private Exam theExpectedExam;

    @Before
    public void setUp() throws Exception {
        FirebaseDatabaseFactory.setTestMode();
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
        graderRepository = new GraderRepoFactory().create();
        graderRepository.create(new Grader("bob", BOB_ID));
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
        theExpectedExam = exams.get(0);
        tearDown();
        AuthenticationHandlerFactory.getTest().authenticate("bobexamscanner80@gmail.com", "Ycombinator").subscribe(to);

        imageProcessor = new ImageProcessorStub();
        imageProcessor = new ImageProcessorStub();
    }

    @After
    public void tearDown() throws Exception {
        AppDatabaseFactory.tearDownDb();
        RemoteDatabaseFacadeFactory.tearDown();
        ExamRepositoryFactory.tearDown();
        CommunicationFacadeFactory.tearDown();
        GraderRepoFactory.tearDown();
    }

    @Test
    public void CreateExamUpdatesGraderTest() {
        assertTrue(examRepository.get(e->true).contains(theExpectedExam));
    }
}
