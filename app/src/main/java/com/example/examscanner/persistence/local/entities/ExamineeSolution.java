package com.example.examscanner.persistence.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"examineeId"},unique = true)})
public class ExamineeSolution {
    public static final String pkName = "id";
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long examineeId;
    private long scannedCaptureId;
    public static final String fkSession = "sessionId";
    @ForeignKey(entity = ScanExamSession.class, parentColumns = ScanExamSession.pkName, childColumns = fkSession)
    private long sessionId;

    public ExamineeSolution(long examineeId, long scannedCaptureId, long sessionId) {
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

    public long getId() {
        return id;
    }

    public long getExamineeId() {
        return examineeId;
    }

    public void setExamineeId(long examineeId) {
        this.examineeId = examineeId;
    }

    public long getScannedCaptureId() {
        return scannedCaptureId;
    }

    public void setScannedCaptureId(long scannedCaptureId) {
        this.scannedCaptureId = scannedCaptureId;
    }

    public void setId(long id) {
        this.id = id;
    }
}
