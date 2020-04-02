package com.example.examscanner.persistence.entities;

import com.example.examscanner.persistence.daos.DaoAbstractTest;
import com.example.examscanner.persistence.entities.relations.ExamineeSolutionWithExamineeAnswers;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class QuestionExamineeSolutionCrossResTest extends DaoAbstractTest {


    private static final String TAG = "QuestionExamineeSolutionCrossResTest";

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testGettingExamineeAnswers() {
        int bmId =0;
        long sid = db.getSessionDao().insert(new Session(TAG));
        long examId = db.getExamDao().insert(new Exam("COMP A",0,"2020", "url", 0,sid));
        long esId = db.getExamineeSolutionDao().insert(new ExamineeSolution(examId, bmId,sid));
        long verId = db.getVersionDao().insert(new Version(0, examId));
        long qId1 = db.getQuestionDao().insert(new Question(1,verId, 3,0,1,2,3));
        long qId2 = db.getQuestionDao().insert(new Question(2,verId, 4,0,1,2,3));
        db.getExamineeAnswerDao().insert(new ExamineeAnswer(qId1,esId, 3,0,0,0,0));
        db.getExamineeAnswerDao().insert(new ExamineeAnswer(qId2,esId, 3,0,0,0,0));
        List<ExamineeSolutionWithExamineeAnswers> eas = db.getExamineeSolutionDao().getExamineeSolutionWithExamineeAnswers();
        List<ExamineeAnswer> theAnswers = null;
        for (ExamineeSolutionWithExamineeAnswers curr:eas) {
            if(curr.getExamineeSolution().getId()==esId){
                theAnswers = curr.getExamineeAnswers();
            }
        }
        assertTrue(theAnswers.size()==2);
    }
}