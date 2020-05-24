package com.example.examscanner.components.create_exam.view_model.integration_with_remote_db;

import android.graphics.Bitmap;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.authentication.state.StateHolder;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.components.create_exam.CreateExamModelView;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.example.examscanner.persistence.local.files_management.FilesManagerFactory;
import com.example.examscanner.persistence.remote.FirebaseDatabaseFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.persistence.remote.files_management.RemoteFilesManagerFactory;
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

import java.nio.file.NoSuchFileException;
import java.util.List;

import io.reactivex.observers.TestObserver;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static com.example.examscanner.components.create_exam.CreateExamFragmentAbstractTestStateFull.BOB_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class CreateExamUpdatesGraderTest {
    private CreateExamModelView out;
    private Repository<Exam> examRepository;
    private Repository<Grader> graderRepository;
    private ImageProcessingFacade imageProcessor;
    private Exam theExpectedExam;

    @Before
    public void setUp() throws Exception {
        FirebaseDatabaseFactory.setTestMode();
        AppDatabaseFactory.setTestMode();
        FilesManagerFactory.setTestMode(getApplicationContext());
        RemoteFilesManagerFactory.setTestMode();
        TestObserver to = new TestObserver(){
            @Override
            public void onNext(Object value) {
                StateFactory.get().login(StateHolder.getDefaultHolder(), value);
            }
        };
        AuthenticationHandlerFactory.getTest().authenticate().subscribe(to);
        to.awaitCount(1);
        ImageProcessingFactory.setTestMode(getApplicationContext());
        imageProcessor = new ImageProcessingFactory().create();
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
        out.holdVersionBitmap(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_in1());
        out.holdVersionNumber(3);
        out.holdGraderUsername("bob");
        out.addGrader();
        out.holdNumOfQuestions("50");
        out.addVersion();
        out.create("CreateExamUpdatesGraderTest_courseName","A","Fall","2020");
        List<Exam> exams = examRepository.get((e)->true);
        assert 1 == exams.size();
        theExpectedExam = exams.get(0);
        theExpectedExam.quziEagerLoad();
//        theExpectedExam.dontResoveFutures();
//        tearDown();
        AppDatabaseFactory.tearDownDb();
        ExamRepositoryFactory.tearDown();
        CommunicationFacadeFactory.tearDown();
        GraderRepoFactory.tearDown();
        AuthenticationHandlerFactory.getTest().authenticate("bobexamscanner80@gmail.com", "Ycombinator").subscribe(to);
        imageProcessor = new ImageProcessorStub();
        imageProcessor = new ImageProcessorStub();
    }

    @After
    public void tearDown() throws Exception {
        AppDatabaseFactory.tearDownDb();
        RemoteDatabaseFacadeFactory.tearDown();
        ExamRepositoryFactory.tearDown();
        FilesManagerFactory.tearDown();
        CommunicationFacadeFactory.tearDown();
        GraderRepoFactory.tearDown();
    }

    @Test
    public void createExamUpdatesGraderTest() {
        final List<Exam> exams = examRepository.get(e -> true);
        assertEquals(1,exams.size());
        Exam theActualExam = exams.get(0);
        assertTrue(theActualExam.getVersions().size()>0);
        assertEquals(theExpectedExam, theActualExam);
    }

    @Test
    public void createExamUpdatesGraderGetsBitmap() {
        List<Exam> exams = null;
        try {
            exams = examRepository.get(e -> true);
        }catch (Exception e){
            throw  e;
//            e.printStackTrace();
//            fail("Probably no such file exception");
        }
        Bitmap expected = theExpectedExam.getVersions().get(0).getPerfectImage();
        Bitmap actual = exams.get(0).getVersions().get(0).getPerfectImage();

        assertTrue(expected.sameAs(actual));
    }
}
