package com.example.examscanner.persistence.entities.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.examscanner.persistence.entities.ExamineeSolution;
import com.example.examscanner.persistence.entities.QuestionExamineeSolutionCrossRes;
import com.example.examscanner.persistence.entities.Question;

import java.util.List;

public class QuestionWithExamineeSolutions {
    @Embedded private Question question;
    @Relation(
            parentColumn = QuestionExamineeSolutionCrossRes.pkQid,
            entityColumn = QuestionExamineeSolutionCrossRes.pkESid,
            associateBy = @Junction(QuestionExamineeSolutionCrossRes.class)
    )
    private List<ExamineeSolution> examineeSolutions;

    public QuestionWithExamineeSolutions(Question question, List<ExamineeSolution> examineeSolutions) {
        this.question = question;
        this.examineeSolutions = examineeSolutions;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<ExamineeSolution> getExamineeSolutions() {
        return examineeSolutions;
    }

    public void setExamineeSolutions(List<ExamineeSolution> examineeSolutions) {
        this.examineeSolutions = examineeSolutions;
    }
}
