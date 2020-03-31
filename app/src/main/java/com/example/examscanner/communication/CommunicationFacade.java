package com.example.examscanner.communication;

import android.graphics.Bitmap;

import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ExamineeSolutionsEntityInterface;
import com.example.examscanner.communication.entities_interfaces.SemiScannedCaptureEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface CommunicationFacade {
    public JSONObject getExam(long id);
    public JSONObject getVersion(int id);
    public JSONArray getExams();
    public JSONObject getGrader(int id);
    public long createSemiScannedCapture(int leftMostX, int upperMostY, int rightMostX, int rightMostY, long sessionId, long versionId, Bitmap bm);
    public long getNewSession(String desctiprion);
    public SemiScannedCaptureEntityInterface getSemiScannedCapture(long id);
    public List<SemiScannedCaptureEntityInterface> getSemiScannedCaptureBySession(long sId);
    public List<ExamineeSolutionsEntityInterface> getExamineeSolutionsBySession(long sId);
    public void uploadExamineesSolutions(long[] ids);
    public ExamEntityInterface getExamBySession(long sId);
    public VersionEntityInterface getVersionByExamIdAndNumber(long eId, int num);
}
