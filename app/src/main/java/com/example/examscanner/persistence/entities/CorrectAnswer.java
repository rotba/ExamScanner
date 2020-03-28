package com.example.examscanner.persistence.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"verId", "questionId"},unique = true)})
public class CorrectAnswer {
    public static final String pkName = "id";
    @PrimaryKey(autoGenerate = true)
    private int id;
    public static final String fkVer = "verId";
    private long verId;
    public static final String fkQue = "questionId";
    private long questionId;
    private int ans;

    public CorrectAnswer(long verId, long questionId, int ans) {
        this.verId = verId;
        this.questionId = questionId;
        this.ans = ans;
    }

    public int getId() {
        return id;
    }



    public long getVerId() {
        return verId;
    }

    public void setVerId(long verId) {
        this.verId = verId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public int getAns() {
        return ans;
    }

    public void setAns(int ans) {
        this.ans = ans;
    }
}
