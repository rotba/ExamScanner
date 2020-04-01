package com.example.examscanner.communication;

import android.graphics.Bitmap;

import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.communication.entities_interfaces.SemiScannedCaptureEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;

import org.json.JSONArray;
import org.json.JSONObject;

public interface CommunicationFacade {
    public JSONObject getExamGoingToEarse(long id);
    public JSONObject getVersionGoingToEarse(int id);
    public JSONArray getExamsGoingToEarse();
    public JSONObject getGraderGoingToEarse(int id);
    public long createExam(String courseName, String url, String year, int term, int semester, long sessionId);
    public long getExamIdBySession(long sId);
    public ExamEntityInterface getExamById(long id);
    public long createSemiScannedCapture(int leftMostX, int upperMostY, int rightMostX, int rightMostY, long sessionId, long versionId, Bitmap bm);
    public long createNewSession(String desctiprion);
    public SemiScannedCaptureEntityInterface getSemiScannedCapture(long id);
    public long[] getSemiScannedCaptureBySession(long sId);
    public long[] getExamineeSolutionsBySession(long sId);
    public long addExamineeAnswer(long solutionId, long questionId ,int ans, int leftX, int upY, int rightX, int botY);
    public long[] getExamineeAnswersIdsByExamineeSolutionId(long esId);
    public void uploadExamineesSolutions(long[] ids);
    public VersionEntityInterface getVersionByExamIdAndNumber(long eId, int num);
    long createVersion(long examId, int versionNumber);
    long addQuestion(long vId, int qNum, int correctAnswer);
    long createExamineeSolution(long sId, Bitmap bm, long examineeId);
    long[] getQuestionsByVersionId(long vId);
}
