package com.example.examscanner.persistence.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"verId", "questionNum"},unique = true)})
public class Question {
    public static final String pkName = "id";
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int questionNum;
    public static final String fkVer = "verId";
    @ForeignKey(entity = Version.class, parentColumns =Version.pkName , childColumns = "verId")
    private long verId;
    private int correctAns;

    public Question(int questionNum, long verId, int correctAns) {
        this.questionNum = questionNum;
        this.verId = verId;
        this.correctAns = correctAns;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public long getVerId() {
        return verId;
    }

    public void setVerId(long verId) {
        this.verId = verId;
    }

    public int getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(int correctAns) {
        this.correctAns = correctAns;
    }
}
