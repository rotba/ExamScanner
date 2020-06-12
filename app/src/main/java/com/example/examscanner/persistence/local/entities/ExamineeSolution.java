package com.example.examscanner.persistence.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        indices = {@Index(value = {"examineeId"},unique = true)}
)
public class ExamineeSolution {
    public static final String pkName = "id";
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String examineeId;
    public static final String fkSession = "sessionId";
    public static final String fkVersion = "versionId";
    @ForeignKey(entity = ScanExamSession.class, parentColumns = ScanExamSession.pkName, childColumns = fkSession)
    private long sessionId;
    @ForeignKey(entity = Version.class, parentColumns = Version.pkName, childColumns = fkVersion)
    private long versionId;
    private String remoteId;

    public ExamineeSolution(String examineeId, long sessionId, long versionId, String remoteId) {
        this.examineeId = examineeId;
        this.sessionId = sessionId;
        this.versionId = versionId;
        this.remoteId = remoteId;
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

    public void setId(long id) {
        this.id = id;
    }

    public long getVersionId() {
        return versionId;
    }

    public void setVersionId(long versionId) {
        this.versionId = versionId;
    }

    public String getBitmapPath() {
        return "SOLUTION_"+String.valueOf(id);
    }

    public String getRemoteId() {
        return remoteId;
    }
    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }


}
