package com.example.examscanner.communication;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.communication.entities_interfaces.QuestionEntityInterface;
import com.example.examscanner.communication.entities_interfaces.SemiScannedCaptureEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.persistence.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class CommunicationFacadeTest {
    CommunicationFacade oot;
    AppDatabase db;

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        RealFacadeImple.setDBTestInstance(db);
        oot = new CommunicationFacadeFactory().create();
    }

    @After
    public void tearDown() throws Exception {
        db.clearAllTables();
    }

    private long setUpSessionContext() {
        return oot.createNewSession("TEST");
    }

    private ExamContext setUpExamContext() {
        long sId = setUpSessionContext();
        return new ExamContext(
                oot.createExam("COMP", "walla.co.il", "2020", 1, 1, sId),
                sId
        );
    }

    private VersionContext setUpVersionContext() {
        ExamContext context = setUpExamContext();
        final int versionNumber = 1;
        return new VersionContext(oot.addVersion(context.eId, versionNumber), context.sId, context.eId, versionNumber);
    }

    private VersionContext setUpVersionNonEmptyContext() {
        ExamContext context = setUpExamContext();
        final int versionNumber = 1;
        VersionContext ans = new VersionContext(oot.addVersion(context.eId, versionNumber), context.sId,context.eId,versionNumber);
        oot.addQuestion(ans.vId, 1, 1,0,0,0,0);
        oot.addQuestion(ans.vId, 2, 400,0,0,0,0);
        oot.addQuestion(ans.vId, 3, 5,0,0,0,0);
        oot.addQuestion(ans.vId, 4, 1,0,0,0,0);
        oot.addQuestion(ans.vId, 5, 2,0,0,0,0);
        return ans;
    }

    private ExamineeSolutionContext setUpExamineeSolutionContextWithNonEmptyVersion() {
        VersionContext versionContext = setUpVersionNonEmptyContext();
        return new ExamineeSolutionContext(
                0,
                oot.createExamineeSolution(versionContext.sId, BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), 0),
                versionContext.vId
        );
    }

    @Test
    public void testCreateAndGetExamNotNull() {
        long id = oot.createExam("COMP", "walla.co.il", "2020", 1, 1, -1);
        assertNotNull(oot.getExamById(id));
    }

    @Test
    public void testAddVersionNotNull() {
        long examId = setUpExamContext().eId;
        final int versionNumber = 1;
        long vId = oot.addVersion(examId, versionNumber);
        assertNotNull(oot.getVersionByExamIdAndNumber(examId, versionNumber));
    }

    @Test
    public void testAddVersionDataIsCorrect() {
        long examId = setUpExamContext().eId;
        final int versionNumber = 6;
        long vId = oot.addVersion(examId, versionNumber);
        VersionEntityInterface vEI = oot.getVersionByExamIdAndNumber(examId, versionNumber);
        assertEquals(vEI.getExamId(), examId);
        assertEquals(vEI.getNumber(), versionNumber);
        assertArrayEquals(oot.getExamById(examId).getVersionsIds(), new long[]{vId});
    }

    @Test
    public void testSessionIdUpdates() {
        assertTrue(oot.createNewSession("hey") != oot.createNewSession("brother"));
    }

    @Test
    public void testFetExamBySession() {
        long sId = setUpSessionContext();
        long eId = oot.createExam("", "", "", 0, 0, sId);
        assertEquals(eId, oot.getExamIdBySession(sId));
    }

    @Test
    public void testCreateAndGetSemiScannedCaptureNotNull() {
        VersionContext context = setUpVersionContext();
        long sscId = oot.createSemiScannedCapture(0, 0, 0, 0, context.sId, context.vId, BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
        assertNotNull(oot.getSemiScannedCapture(sscId));
    }

    @Test
    public void testCreateAndGetBySessionSemiScannedCaptureNotNull() {
        VersionContext context = setUpVersionContext();
        long sscId = oot.createSemiScannedCapture(0, 0, 0, 0, context.sId, context.vId, BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
        boolean assertion = false;
        long[] sscs = oot.getSemiScannedCaptureBySession(context.sId);
        for (int i = 0; i < sscs.length; i++) {
            assertion |= sscs[i] == sscId;
        }
        assertTrue(assertion);
    }

    @Test
    public void testWeirdBug() {
        setUpVersionContext();
        VersionContext context2 = setUpVersionContext();
        long sscId = oot.createSemiScannedCapture(0, 0, 0, 0, context2.sId, context2.vId, BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
        boolean assertion = false;
        long[] sscs = oot.getSemiScannedCaptureBySession(context2.sId);
        for (int i = 0; i < sscs.length; i++) {
            assertion |= sscs[i] == sscId;
        }
        assertTrue(assertion);
    }

    @Test
    public void testGetAndCreateExamineeSolutionsBySessionNotEmpty() {
        VersionContext vContext = setUpVersionContext();
        long esId = oot.createExamineeSolution(vContext.sId, BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), 0);
        assertTrue(oot.getExamineeSolutionsBySession(vContext.sId).length > 0);
    }

    @Test
    public void testGetAndCreateExamineeSolutionsBySessionUpdates() {
        VersionContext vContext = setUpVersionContext();
        long esId = oot.createExamineeSolution(vContext.sId, BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), 0);
        boolean assertion = false;
        long[] ess = oot.getExamineeSolutionsBySession(vContext.sId);
        for (int i = 0; i < ess.length; i++) {
            assertion |= ess[i] == esId;
        }
        assertTrue(assertion);
    }

    @Test
    public void testGetAndCreateExamineeAnswersByExamineeSolutionNotNull() {
        ExamineeSolutionContext esContext = setUpExamineeSolutionContextWithNonEmptyVersion();
        long[] qs = oot.getQuestionsByVersionId(esContext.vId);
        long eaId = oot.addExamineeAnswer(esContext.solutionId, qs[0], 1, 0, 0, 0, 0);
        assertTrue(oot.getExamineeAnswersIdsByExamineeSolutionId(esContext.solutionId).length > 0);
    }

    @Test
    public void testGetAndCreateExamineeAnswersByExamineeSolutionDataUpdates() {
        ExamineeSolutionContext esContext = setUpExamineeSolutionContextWithNonEmptyVersion();
        long[] qs = oot.getQuestionsByVersionId(esContext.vId);
        long eaId = oot.addExamineeAnswer(esContext.solutionId, qs[0], 1, 0, 0, 0, 0);
        long[] answers = oot.getExamineeAnswersIdsByExamineeSolutionId(esContext.solutionId);
        boolean assertion = false;
        for (int i = 0; i < answers.length; i++) {
            assertion |= answers[i] == eaId;
        }
        assertTrue(assertion);
    }
    @Test
    public void testSemiScannedCaptureEntityInterface() {
        VersionContext versionContext = setUpVersionContext();
        final Bitmap testJpg1Marked = BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked();
        final int rightMostY = 1;
        final int rightMostX = 2;
        final int upperMostY = 3;
        final int leftMostX = 4;
        oot.createSemiScannedCapture(leftMostX, upperMostY, rightMostX, rightMostY,versionContext.sId,versionContext.vId, testJpg1Marked);
        SemiScannedCaptureEntityInterface ei = oot.getSemiScannedCapture(
                oot.getSemiScannedCaptureBySession(versionContext.sId)[0]
        );
        assertTrue(ei.getBitmap().sameAs(testJpg1Marked));
        assertEquals(ei.getSessionId(),versionContext.sId);
        assertEquals(ei.getLeftMostX(),leftMostX);
        assertEquals(ei.getUpperMostY(),upperMostY);
        assertEquals(ei.getRightMostX(),rightMostX);
        assertEquals(ei.getBottomMosty(),rightMostY);
    }

    @Test
    public void testVersionEntityInterface() {
        ExamContext examContext = setUpExamContext();
        final int versionNumber = 1;
        oot.addVersion(examContext.eId, versionNumber);
        VersionEntityInterface ei = oot.getVersionByExamIdAndNumber(examContext.eId,versionNumber);
        assertEquals(ei.getNumber(),versionNumber);
        assertEquals(ei.getExamId(),examContext.eId);
        assertArrayEquals(ei.getQuestions(),new long[0]);
    }
    @Test
    public void testExamEntityInterface() {
        long sId = setUpSessionContext();
        final String comp = "COMP";
        final String url = "walla.co.il";
        final String year = "2020";
        final int term = 1;
        final int semester = 1;
        final int sessionId = -1;
        long id = oot.createExam(comp, url, year, term, semester, sessionId);
        ExamEntityInterface ei = oot.getExamById(id);
        assertEquals(ei.getCourseName(), comp);
        assertEquals(ei.getUrl(), url);
        assertEquals(ei.getTerm(), term);
        assertEquals(ei.getSemester(), semester);
        assertEquals(ei.getSessionId(), sessionId);
        assertArrayEquals(ei.getVersionsIds(), new long[0]);
    }
    @Test
    public void testQuestionEntityInterface() {
        VersionContext versionContext = setUpVersionContext();
        final int qNum = 3;
        final int correctAnswer = 4;
        final int leftX = 1;
        final int upY = 2;
        final int rightX = 3;
        final int bottomY = 4;
        oot.addQuestion(versionContext.vId, qNum, correctAnswer, leftX, upY, rightX, bottomY);
        QuestionEntityInterface ei = oot.getQuestionByExamIdVerNumAndQNum(versionContext.eId,versionContext.vNum,qNum);
        assertEquals(ei.getLeftX(),leftX);
        assertEquals(ei.getUpY(),upY);
        assertEquals(ei.getBottomY(),bottomY);
        assertEquals(ei.getRightX(),rightX);
        assertEquals(ei.getCorrectAnswer(),correctAnswer);
        assertEquals(ei.getVersionId(),versionContext.vId);
    }
    @Test
    public void testExamineeAnswerEntityInterface() {

    }



    private class VersionContext {
        long vId;
        long sId;
        long eId;
        int vNum;

        public VersionContext(long vId, long sId, long eId, int vNum) {
            this.vId = vId;
            this.sId = sId;
            this.eId =eId;
            this.vNum = vNum;
        }

    }

    private class ExamContext {
        long eId;
        long sId;

        public ExamContext(long eId, long sId) {
            this.eId = eId;
            this.sId = sId;
        }
    }

    private class ExamineeSolutionContext {
        long vId;
        long solutionId;
        int esId;

        public ExamineeSolutionContext(int examineeId, long solutionId,long vId) {
            this.vId = vId;
            this.solutionId = solutionId;
            this.esId = examineeId;
        }
    }
}