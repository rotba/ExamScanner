package com.example.examscanner.repositories.version;

import com.example.examscanner.repositories.exam.Exam;

public class Version {
    private Exam exam;
    private int[] answers;
    private long id;

    public Version(Exam exam, int[] answers, long id) {
        this.exam = exam;
        this.answers = answers;
        this.id = id;
    }

    public boolean isAssociatedWith(int examId) {
        return exam.getId()==examId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
