package com.example.examscanner.communication;

import android.graphics.Bitmap;

import com.example.examscanner.communication.entities.SemiScannedCaptureEntityInterface;

import org.json.JSONArray;
import org.json.JSONObject;

public interface CommunicationFacade {
    public JSONObject getExam(int id);
    public JSONObject getVersion(int id);
    public JSONArray getExams();
    public JSONObject getGrader(int id);
    public long createSemiScannedCapture(int leftMostX, int upperMostY, int rightMostX, int rightMostY, long sessionId, long versionId, Bitmap bm);
    public long getNewSession(String desctiprion);
    public SemiScannedCaptureEntityInterface getSemiScannedCapture(long id);
}
