package com.example.examscanner.persistence.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"examId", "verNum"}, unique = true)})
public class Version {
    public static final String pkName = "id";

    @PrimaryKey(autoGenerate = true)
    private int id;

    public static final String fkExam = "examId";
    private long examId;
    private int verNum;

    public Version(int verNum, long examId) {
        this.verNum = verNum;
        this.examId = examId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
