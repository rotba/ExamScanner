package com.example.examscanner.communication;

import android.graphics.Bitmap;

import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ExamineeSolutionsEntityInterface;
import com.example.examscanner.communication.entities_interfaces.SemiScannedCaptureEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FacadeImplProxy implements CommunicationFacade {
    private RealFacadeImple realImpl = RealFacadeImple.getInstance();
    @Override
    public JSONObject getExam(long id) {
        if (useReal()){
            return realImpl.getExam(id);
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
    public JSONObject getVersion(int id) {
        if (useReal()){
            return realImpl.getExam(id);
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
    public JSONObject getGrader(int id) {
        return null;
    }

    @Override
    public long createSemiScannedCapture(int leftMostX, int upperMostY, int rightMostX, int rightMostY, long sessionId, long versionId, Bitmap bm) {
        return realImpl.createSemiScannedCapture(leftMostX, upperMostY, rightMostX, rightMostY,  sessionId,  versionId, bm);
    }



    @Override
    public long getNewSession(String desctiprion) {
        return 0;
    }

    @Override
    public SemiScannedCaptureEntityInterface getSemiScannedCapture(long id) {
        return null;
    }

    @Override
    public List<SemiScannedCaptureEntityInterface> getSemiScannedCaptureBySession(long sId) {
        return null;
    }

    @Override
    public List<ExamineeSolutionsEntityInterface> getExamineeSolutionsBySession(long sId) {
        return null;
    }

    @Override
    public void uploadExamineesSolutions(long[] ids) {

    }

    @Override
    public ExamEntityInterface getExamBySession(long sId) {
        return null;
    }

    @Override
    public VersionEntityInterface getVersionByExamIdAndNumber(long eId, int num) {
        return null;
    }

    private boolean useReal(){return false;}

    @Override
    public JSONArray getExams() {
        JSONArray ans =  new JSONArray();
        ans.put(getExam(0));
        return ans;
    }
}
