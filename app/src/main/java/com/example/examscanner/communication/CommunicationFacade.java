package com.example.examscanner.communication;

import android.graphics.Bitmap;

import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ExamineeAnswerEntityInterface;
import com.example.examscanner.communication.entities_interfaces.QuestionEntityInterface;
import com.example.examscanner.communication.entities_interfaces.SemiScannedCaptureEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;

public interface CommunicationFacade {
    public long createExam(String courseName, String url, String year, int term, int semester, long sessionId);
    public long getExamIdBySession(long sId);
    public long createSemiScannedCapture(int leftMostX, int upperMostY, int rightMostX, int rightMostY, long sessionId, Bitmap bm);
    public long createNewSession(String desctiprion);
    public long addExamineeAnswer(long solutionId, long questionId ,int ans, int leftX, int upY, int rightX, int botY);
    public long addVersion(long examId, int versionNumber);
    public long addQuestion(long vId, int qNum, int correctAnswer, int leftX, int upY, int rightX, int bottomY);
    public long createExamineeSolution(long sId, Bitmap bm, long examineeId);
    public long[] getSemiScannedCaptureBySession(long sId);
    public long[] getExamineeSolutionsBySession(long sId);
    public long[] getExamineeAnswersIdsByExamineeSolutionId(long esId);
    public long[] getQuestionsByVersionId(long vId);
    public void uploadExamineesSolutions(long[] ids);
    public SemiScannedCaptureEntityInterface getSemiScannedCapture(long id);
    public VersionEntityInterface getVersionByExamIdAndNumber(long eId, int num);
    public ExamEntityInterface getExamById(long id);
    public QuestionEntityInterface getQuestionByExamIdVerNumAndQNum(long examId, int verNum, int qNum);
    public ExamineeAnswerEntityInterface getExamineeAnswerByExamIdVerNumAndQNumAndExamineeId(long examId, int verNum, int qNum, long examineeId);
    public ExamEntityInterface[] getExams();
    public void updateExam(long id, String courseName, int semester, int term, long[] versions, long sessionId, String year);
}
