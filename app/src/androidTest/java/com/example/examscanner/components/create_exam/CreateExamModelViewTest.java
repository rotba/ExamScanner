package com.example.examscanner.components.create_exam;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.persistence.AppDatabaseFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.question.QuestionRepositoryFactory;
import com.example.examscanner.repositories.version.Version;
import com.example.examscanner.repositories.version.VersionRepoFactory;
import com.example.examscanner.stubs.ImageProcessorStub;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CreateExamModelViewTest {

    private CreateExamModelView out;
    private Repository<Version> versionRepository;
    private Repository<Exam> examRepository;
    private ImageProcessorStub imageProcessor;

    @Before
    public void setUp() throws Exception {
        AppDatabaseFactory.setTestMode();
        imageProcessor = new ImageProcessorStub();
        out  = new CreateExamModelView(
                new ExamRepositoryFactory().create(),
                new VersionRepoFactory().create(),
                new QuestionRepositoryFactory().create(),
                imageProcessor,
                0
        );
        versionRepository = new VersionRepoFactory().create();
        examRepository = new ExamRepositoryFactory().create();
    }

    @After
    public void tearDown() throws Exception {
        AppDatabaseFactory.tearDownDb();
    }

    @Test
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
        out.addVersion();
        Exam exam = examRepository.get(out.getExam().getId());
        assertTrue(exam.getVersions().length==1);
        long verId = exam.getVersions()[0];
        Version version = versionRepository.get(verId);
        assertEquals(expectedNumOfAnswers[0] ,version.getQuestions().length);
    }


}