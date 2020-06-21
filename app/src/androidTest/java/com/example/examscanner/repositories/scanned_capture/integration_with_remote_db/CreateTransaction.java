package com.example.examscanner.repositories.scanned_capture.integration_with_remote_db;

import android.Manifest;

import androidx.test.rule.GrantPermissionRule;

import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.State;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.communication.ContextProvider;
import com.example.examscanner.communication.tasks.Continuation;
import com.example.examscanner.communication.tasks.IdsGenerator;
import com.example.examscanner.communication.tasks.TasksManager;
import com.example.examscanner.communication.tasks.TasksManagerFactory;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.example.examscanner.persistence.local.files_management.FilesManagerFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacade;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.persistence.remote.entities.ExamineeSolution;
import com.example.examscanner.persistence.remote.entities.Version;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.RepositoryException;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamInstancesGenerator;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.grader.GraderRepoFactory;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.stubs.FilesManagerStub;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.observers.TestObserver;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CreateTransaction {
    private static String examineeId;
    Repository<Exam> eRepo;
    Repository<ScannedCapture> out1;
    RemoteDatabaseFacade out2;
    ImageProcessingFacade ip;
    private Exam theExam;

    @Rule
    public GrantPermissionRule write = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule rule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);


    @Before
    public void setUp() throws Exception {
        ContextProvider.set(getInstrumentation().getContext());
        AppDatabaseFactory.setTestMode();
        getState();
        FilesManagerFactory.setStubInstance(new FilesManagerStub());
        eRepo = new ExamRepositoryFactory().create();
        theExam = ExamInstancesGenerator.getInstance1();
        eRepo.create(theExam);
        ImageProcessingFactory.setTestMode(getApplicationContext());
        ip = new ImageProcessingFactory().create();
        examineeId = String.valueOf(new Random().nextLong());
    }

    @After
    public void tearDown() throws Exception {
        AppDatabaseFactory.tearDownDb();
//        RemoteDatabaseFacadeFactory.tearDown();
        CommunicationFacadeFactory.tearDown();
//        RemoteFilesManagerFactory.tearDown();
        ExamRepositoryFactory.tearDown();
        CDCRepositoryFacrory.tearDown();
        ScannedCaptureRepositoryFactory.tearDown();
        GraderRepoFactory.tearDown();
    }

    private void tearDown(String versionId) {
        for (Version v : out2.getVersions().blockingFirst()) {
            if (v._getId().equals(versionId)) {
                out2.deleteExam(v.examId);
            }
        }
    }

    @NotNull
    private State getState() {
        State[] ss = new State[1];
        TestObserver<FirebaseAuth> observer = new TestObserver<FirebaseAuth>() {
            @Override
            public void onNext(FirebaseAuth firebaseAuth) {
                StateFactory.get().login(s -> ss[0] = s, firebaseAuth);
            }
        };
        AuthenticationHandlerFactory.getTest().authenticate().subscribe(observer);
        observer.awaitCount(1);
        return ss[0];
    }

    @Test
    public void testSolutionIsUploadingFine() {
        out1 = new ScannedCaptureRepositoryFactory().create();
        ScannedCapture[] scs = new ScannedCapture[1];
        ip.scanAnswers(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), 53, new ScanAnswersConsumer() {
            @Override
            public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                try {
                    int[] ansDet = {1};
                    int[] sel = {1};
                    scs[0] = new ScannedCapture(-1, BitmapsInstancesFactoryAndroidTest.getTestJpg1(), BitmapsInstancesFactoryAndroidTest.getTestJpg1(), 1, 1, ansDet, new float[1], new float[1], new float[1], new float[1], sel, theExam.getVersions().get(0), examineeId);
                    out1.create(
                            scs[0]
                    );
                } catch (RepositoryException e) {

                }
            }
        });
        out2 = RemoteDatabaseFacadeFactory.get();
        List<ExamineeSolution> remoteSolutions = new ArrayList<>();
        TasksManager tasks = TasksManagerFactory.get();
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scs[0].approve();
        try {
            Thread.sleep(7 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tasks.get(IdsGenerator.forSolution(out1.get(x -> true).get(0).getId())).addContinuation(() -> {
                    out2.getExamineeSolutions().blockingSubscribe(l -> remoteSolutions.addAll(l), throwable -> {
                        throw new RuntimeException(throwable);
                    });
                    for (ExamineeSolution es : remoteSolutions) {
                        if (es.examineeId.equals(examineeId)) {
                            assertNotEquals(es._getId(), "null");
                            assertEquals(es.isValid, true);
                            tearDown(es.versionId);
                            return;
                        }
                    }
                },
                Continuation.ShouldNotHappenException());
    }


}
