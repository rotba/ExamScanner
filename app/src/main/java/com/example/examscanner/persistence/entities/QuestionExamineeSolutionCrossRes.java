package com.example.examscanner.persistence.entities;

import androidx.room.Entity;

@Entity(primaryKeys = {"examineeSolutionId","questionId"})
public class QuestionExamineeSolutionCrossRes {
    public static final String pkESid = "examineeSolutionId";
    private long examineeSolutionId;
    public static final String pkQid = "questionId";
    private long questionId;

    public QuestionExamineeSolutionCrossRes(long examineeSolutionId, long questionId) {
        this.examineeSolutionId = examineeSolutionId;
        this.questionId = questionId;
    }

    public long getExamineeSolutionId() {
        return examineeSolutionId;
    }

    public void setExamineeSolutionId(long examineeSolutionId) {
        this.examineeSolutionId = examineeSolutionId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

}
