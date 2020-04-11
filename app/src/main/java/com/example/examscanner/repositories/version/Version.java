package com.example.examscanner.repositories.version;

import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamConverter;
import com.example.examscanner.repositories.question.Question;

import java.util.List;
import java.util.concurrent.Future;

public class Version {
    private Exam exam;
    private long id;
    private int num;
    private Future<List<Question>> fQuestions;


    public Version(int num, Exam e) {
        this.num = num;
        this.exam = e;
    }

    public int getNum() {
        return num;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


    public void setQuestionsFuture(Future<List<Question>> qf) {
        this.fQuestions = qf;
    }

    public Exam getExam() {
        return exam;
    }
}
