package com.example.examscanner.persistence.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"examineeSolutionId", "questionId"},unique = true)})
public class ExamineeAnswer {
    public static final String pkName = "id";
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ForeignKey(entity = Question.class, parentColumns =Question.pkName , childColumns ="questionId" )
    public static final String fkQue = "questionId";
    private long questionId;
    public static final String fkESid = "examineeSolutionId";
    @ForeignKey(entity = ExamineeSolution.class, parentColumns =ExamineeSolution.pkName , childColumns ="examineeSolutionId" )
    private long examineeSolutionId;
    private int ans;

    public ExamineeAnswer(long questionId, long examineeSolutionId, int ans) {
        this.questionId = questionId;
        this.examineeSolutionId = examineeSolutionId;
        this.ans = ans;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public long getExamineeSolutionId() {
        return examineeSolutionId;
    }

    public void setExamineeSolutionId(long examineeSolutionId) {
        this.examineeSolutionId = examineeSolutionId;
    }

    public int getAns() {
        return ans;
    }

    public void setAns(int ans) {
        this.ans = ans;
    }
}
