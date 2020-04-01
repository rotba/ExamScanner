package com.example.examscanner.communication;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.persistence.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

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
    public void closeDb() throws IOException {
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
        return new VersionContext(oot.createVersion(context.eId, 1), context.sId);
    }

    private VersionContext setUpVersionNonEmptyContext() {
        ExamContext context = setUpExamContext();
        VersionContext ans = new VersionContext(oot.createVersion(context.eId, 1), context.sId);
        oot.addQuestion(ans.vId, 1, 1);
        oot.addQuestion(ans.vId, 2, 4);
        oot.addQuestion(ans.vId, 3, 5);
        oot.addQuestion(ans.vId, 4, 1);
        oot.addQuestion(ans.vId, 5, 2);
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
    public void testCreateAndGetExamDataIsCorrect() {
        final String comp = "COMP";
        final String url = "walla.co.il";
        final String year = "2020";
        final int term = 1;
        final int semester = 1;
        final int sessionId = -1;
        long id = oot.createExam(comp, url, year, term, semester, sessionId);
        ExamEntityInterface examEI = oot.getExamById(id);
        assertEquals(examEI.getCourseName(), comp);
        assertEquals(examEI.getUrl(), url);
        assertEquals(examEI.getTerm(), term);
        assertEquals(examEI.getSemester(), semester);
        assertEquals(examEI.getSessionId(), sessionId);
    }

    @Test
    public void testAddVersionNotNull() {
        long examId = setUpExamContext().eId;
        final int versionNumber = 1;
        long vId = oot.createVersion(examId, versionNumber);
        assertNotNull(oot.getVersionByExamIdAndNumber(examId, versionNumber));
    }

    @Test
    public void testAddVersionDataIsCorrect() {
        long examId = setUpExamContext().eId;
        final int versionNumber = 1;
        long vId = oot.createVersion(examId, versionNumber);
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


    private class VersionContext {
        long vId;
        long sId;

        public VersionContext(long vId, long sId) {
            this.vId = vId;
            this.sId = sId;
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