package com.example.examscanner.repositories.version;

import com.example.examscanner.repositories.exam.Exam;

public class Version {
    private Exam exam;
    private int[] answers;

    private long id;

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
