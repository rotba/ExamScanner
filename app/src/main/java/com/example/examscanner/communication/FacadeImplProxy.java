package com.example.examscanner.communication;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ExamineeAnswerEntityInterface;
import com.example.examscanner.communication.entities_interfaces.QuestionEntityInterface;
import com.example.examscanner.communication.entities_interfaces.SemiScannedCaptureEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FacadeImplProxy implements CommunicationFacade {
    private RealFacadeImple realImpl = RealFacadeImple.getInstance();
    @Override
    public JSONObject getExamGoingToEarse(long id) {
        if (useReal()){
            return realImpl.getExamGoingToEarse(id);
        }
        else {
            try {
                return new JSONObject()
                        .put("id", "0")
                        .put("manager", "0")
                        .put("graders", new JSONArray("[0]"))
                        .put("course_name", "COMP")
                        .put("moed", "A");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public JSONObject getVersionGoingToEarse(int id) {
        if (useReal()){
            return realImpl.getExamGoingToEarse(id);
        }
        else {
            String someAnwersString = "[1,2,3,2,1,3,4,5,3,3,3,3,1,1,1]";
            try {
                return new JSONObject()
                        .put("exam_id", "0")
                        .put("answers", new JSONArray(someAnwersString));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public JSONObject getGraderGoingToEarse(int id) {
        return null;
    }

    @Override
    public long createExam(String courseName, String url, String year, int term, int semester, long sessionId) {
        return realImpl.createExam(courseName, url, year, term, semester, sessionId);
    }

    @Override
    public long createSemiScannedCapture(int leftMostX, int upperMostY, int rightMostX, int rightMostY, long sessionId, long versionId, Bitmap bm) {
        return realImpl.createSemiScannedCapture(leftMostX, upperMostY, rightMostX, rightMostY,  sessionId,  versionId, bm);
    }



    @Override
    public long createNewSession(String desctiprion) {
        return realImpl.createNewSession(desctiprion);
    }

    @Override
    public SemiScannedCaptureEntityInterface getSemiScannedCapture(long id) {
        return realImpl.getSemiScannedCapture(id);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public long[] getSemiScannedCaptureBySession(long sId) {
        return realImpl.getSemiScannedCaptureBySession(sId);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public long[] getExamineeSolutionsBySession(long sId) {
        return realImpl.getExamineeSolutionsBySession(sId);
    }

    @Override
    public long addExamineeAnswer(long solutionId, long questionId, int ans, int leftX, int upY, int rightX, int botY) {
        return realImpl.addExamineeAnswer(solutionId, questionId, ans, leftX,  upY,  rightX,  botY);
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public long[] getExamineeAnswersIdsByExamineeSolutionId(long esId) {
        return realImpl.getExamineeAnswersIdsByExamineeSolutionId(esId);
    }

    @Override
    public void uploadExamineesSolutions(long[] ids) {

    }

    @Override
    public long getExamIdBySession(long sId) {
        return realImpl.getExamIdBySession(sId);
    }

    @Override
    public ExamEntityInterface getExamById(long id) {
        return realImpl.getExamById(id);
    }

    @Override
    public QuestionEntityInterface getQuestionByExamIdVerNumAndQNum(long examId, int verNum, int qNum) {
        return realImpl.getQuestionByExamIdVerNumAndQNum(examId, verNum, qNum);
    }

    @Override
    public ExamineeAnswerEntityInterface getExamineeAnswerByExamIdVerNumAndQNumAndExamineeId(long examId, int verNum, int qNum, long examineeId) {
        return null;
    }

    @Override
    public VersionEntityInterface getVersionByExamIdAndNumber(long eId, int num) {
        return realImpl.getVersionByExamIdAndNumber(eId, num);
    }

    @Override
    public long addVersion(long examId, int versionNumber) {
        return realImpl.addVersion(examId,versionNumber);
    }


    @Override
    public long addQuestion(long vId, int qNum, int correctAnswer, int leftX, int upY, int rightX, int bottomY) {
        return realImpl.addQuestion(vId, qNum, correctAnswer, leftX, upY, rightX, bottomY);
    }


    @Override
    public long createExamineeSolution(long sId, Bitmap bm, long examineeId) {
        return realImpl.createExamineeSolution(sId, bm, examineeId);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public long[] getQuestionsByVersionId(long vId) {
        return realImpl.getQuestionsByVersionId(vId);
    }

    private boolean useReal(){return false;}

    @Override
    public JSONArray getExamsGoingToEarse() {
        JSONArray ans =  new JSONArray();
        ans.put(getExamGoingToEarse(0));
        return ans;
    }
}
