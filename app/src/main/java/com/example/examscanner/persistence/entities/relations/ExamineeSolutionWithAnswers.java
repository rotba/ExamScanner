package com.example.examscanner.persistence.entities.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.examscanner.persistence.entities.ExamineeSolution;
import com.example.examscanner.persistence.entities.QuestionExamineeSolutionCrossRes;
import com.example.examscanner.persistence.entities.Question;

import java.util.List;

public class ExamineeSolutionWithAnswers {
    @Embedded
    private ExamineeSolution examineeSolution;
    @Relation(
            parentColumn = QuestionExamineeSolutionCrossRes.pkESid,
            entityColumn = QuestionExamineeSolutionCrossRes.pkQid,
            associateBy = @Junction(QuestionExamineeSolutionCrossRes.class)
    )
    private List<Question> questions;

    public ExamineeSolutionWithAnswers(ExamineeSolution examineeSolution, List<Question> questions) {
        this.examineeSolution = examineeSolution;
        this.questions = questions;
    }

    public ExamineeSolution getExamineeSolution() {
        return examineeSolution;
    }

    public void setExamineeSolution(ExamineeSolution examineeSolution) {
        this.examineeSolution = examineeSolution;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
