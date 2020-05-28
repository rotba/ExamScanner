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
    private String examineeId;
    private long scannedCaptureId;
    public static final String fkSession = "sessionId";
    public static final String fkVersion = "versionId";
    @ForeignKey(entity = ScanExamSession.class, parentColumns = ScanExamSession.pkName, childColumns = fkSession)
    private long sessionId;
    @ForeignKey(entity = Version.class, parentColumns = Version.pkName, childColumns = fkVersion)
    private long versionId;

    public ExamineeSolution(String examineeId, long scannedCaptureId, long sessionId, long versionId) {
        this.examineeId = examineeId;
        this.scannedCaptureId = scannedCaptureId;
        this.sessionId = sessionId;
        this.versionId = versionId;
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

    public String getExamineeId() {
        return examineeId;
    }

    public void setExamineeId(String examineeId) {
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

    public long getVersionId() {
        return versionId;
    }

    public void setVersionId(long versionId) {
        this.versionId = versionId;
    }
}
