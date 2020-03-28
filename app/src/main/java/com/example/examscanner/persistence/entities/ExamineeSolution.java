package com.example.examscanner.persistence.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class ExamineeSolution {
    public static final String pkName = "id";
    @PrimaryKey(autoGenerate = true)
    private int id;
    private long examineeId;
    private int scannedCaptureId;
    public static final String fkSession = "sessionId";
    private long sessionId;

    public ExamineeSolution(long examineeId, int scannedCaptureId, long sessionId) {
        this.examineeId = examineeId;
        this.scannedCaptureId = scannedCaptureId;
        this.sessionId = sessionId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public int getId() {
        return id;
    }

    public long getExamineeId() {
        return examineeId;
    }

    public void setExamineeId(long examineeId) {
        this.examineeId = examineeId;
    }

    public int getScannedCaptureId() {
        return scannedCaptureId;
    }

    public void setScannedCaptureId(int scannedCaptureId) {
        this.scannedCaptureId = scannedCaptureId;
    }

    public void setId(int id) {
        this.id = id;
    }
}
