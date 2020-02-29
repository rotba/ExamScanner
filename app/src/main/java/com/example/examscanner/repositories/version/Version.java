package com.example.examscanner.repositories.version;

import com.example.examscanner.repositories.exam.Exam;

public class Version {
    private Exam exam;
    private int[] answers;

    public boolean isAssociatedWith(int examId) {
        return exam.getId()==examId;
    }
}
