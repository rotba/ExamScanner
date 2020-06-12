package com.example.examscanner.communication;

import android.graphics.Bitmap;

import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ExamineeAnswerEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ExamineeSolutionsEntityInterface;
import com.example.examscanner.communication.entities_interfaces.GraderEntityInterface;
import com.example.examscanner.communication.entities_interfaces.QuestionEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ScanExamSessionEntityInterface;
import com.example.examscanner.communication.entities_interfaces.SemiScannedCaptureEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;
import com.example.examscanner.repositories.scanned_capture.Answer;

import java.util.List;

public interface CommunicationFacade {
    public long createExam(String courseName, String url, String year, int term, int semester,String mangerId, String[] gradersIdentifiers ,long sessionId, int numberOfQuestions, int uploaded);
    public long getExamIdByScanExamSession(long sId);
    public long createSemiScannedCapture(int leftMostX, int upperMostY, int rightMostX, int rightMostY, long sessionId, Bitmap bm);
    public long createNewScanExamSession(long examId);
    public long addExamineeAnswer(long solutionId, long questionId ,int ans, int leftX, int upY, int rightX, int botY);
    public long addVersion(long examId, int versionNumber, Bitmap bm);
    public long addQuestion(long vId, int qNum, int correctAnswer, int leftX, int upY, int rightX, int bottomY);
    public long createExamineeSolution(long sId, Bitmap bm, String examineeId, long versionId);
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
    public long createNewCreateExamSession();
    public ScanExamSessionEntityInterface[] getScanExamSessions();
    public  long createQuestion(long versionId, int num, int ans, int left, int up, int right, int bottom);
    public long createVersion(long examId, int num, Bitmap verBm);
    public VersionEntityInterface getVersionById(long vId);
    public QuestionEntityInterface getQuestionById(long qId);
    public long insertVersionReplaceOnConflict(long examId, int num, Bitmap perfectImage);
    public long insertQuestionReplaceOnConflict(long vId, int qNum, int qAns, int left, int right, int up, int bottom);
    public List<GraderEntityInterface> getGraders();
    public void createGrader(String userName, String userId);
    public ExamineeAnswerEntityInterface getAnswerById(long examineeAnswersId);
    public ExamineeSolutionsEntityInterface[] getExamineeSoultions();
    public void addExamineeGrade(long solutionId, long versionId, int[][] answers, float grade);
    public void removeExamineeSolutionFromCache(long id);

    public void deleteExamineeSolution(long id);

    public void updateExamineeAnswer(long solutionId, long questionId ,int ans, int leftX, int upY, int rightX, int botY);
    public void updateUploaded(long id);
    // public ExamEntityInterface[] getExamsofGrader(String userId);
}
