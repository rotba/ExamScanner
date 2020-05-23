package com.example.examscanner.persistence.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"examId", "verNum"}, unique = true)})
public class Version {
    public static final String pkName = "id";

    @PrimaryKey(autoGenerate = true)
    private long id;
    public static final String fkExam = "examId";
    @ForeignKey(entity = Exam.class, parentColumns =Exam.pkName , childColumns =fkExam )
    private long examId;
    private String remoteVersionId;
    private int verNum;

    public Version(int verNum, long examId, String remoteVersionId) {
        this.verNum = verNum;
        this.examId = examId;
        this.remoteVersionId = remoteVersionId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getExamId() {
        return examId;
    }

    public void setExamId(long examId) {
        this.examId = examId;
    }

    public int getVerNum() {
        return verNum;
    }

    public String getRemoteVersionId() {
        return remoteVersionId;
    }

    public String _getBitmapPath(){
        return "VERISION_"+String.valueOf(id);
    }
}
