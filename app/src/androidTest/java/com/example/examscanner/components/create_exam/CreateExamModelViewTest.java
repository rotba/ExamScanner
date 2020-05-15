package com.example.examscanner.components.create_exam;

import android.graphics.Bitmap;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.image_processing.ImageProcessor;
import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.exam.Version;
import com.example.examscanner.stubs.ImageProcessorStub;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CreateExamModelViewTest {

    private CreateExamModelView out;
    private Repository<Exam> examRepository;
    private ImageProcessorStub imageProcessor;
    private ImageProcessingFacade realIP;

    @Before
    public void setUp() throws Exception {
        AppDatabaseFactory.setTestMode();
        imageProcessor = new ImageProcessorStub();
        realIP = new ImageProcessingFactory().create();
        out  = new CreateExamModelView(
                new ExamRepositoryFactory().create(),
                imageProcessor,
                StateFactory.get(),
                0
        );
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
        out.create("testAddVersion()_courseName","A","Fall","2020");
        List<Exam> exams = examRepository.get((e)->true);
        assertEquals(exams.size(),1);
        Exam theExam = exams.get(0);
        assertTrue(theExam.getVersions().size()==1);
        final Version version = theExam.getVersions().get(0);
        assertEquals(expectedNumOfAnswers[0] ,version.getQuestions().size());
    }

    @Test
    public void testAddVersion2RealIP(){
        final int[] expectedNumOfAnswers = new int[1];
        int numOfQuestions = out.getExam().getNumOfQuestions();
        Bitmap bm = BitmapsInstancesFactoryAndroidTest.getExam50Qs();
        realIP.scanAnswers(bm, numOfQuestions, new ScanAnswersConsumer() {
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
        out.create("testAddVersion()_courseName","A","Fall","2020");
        List<Exam> exams = examRepository.get((e)->true);
        assertEquals(exams.size(),1);
        Exam theExam = exams.get(0);
        assertTrue(theExam.getVersions().size()==1);
        final Version version = theExam.getVersions().get(0);
        assertEquals(expectedNumOfAnswers[0] ,version.getQuestions().size());
    }

}