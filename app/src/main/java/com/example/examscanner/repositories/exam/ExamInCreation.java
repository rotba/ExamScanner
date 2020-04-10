package com.example.examscanner.repositories.exam;

import com.example.examscanner.repositories.grader.ExamManager;
import com.example.examscanner.repositories.grader.Grader;
import com.example.examscanner.repositories.version.Version;

import java.util.ArrayList;
import java.util.List;

public class ExamInCreation extends Exam {

    public ExamInCreation(long sessionId) {
        super(
                null,
                new ArrayList<>(),
                "THE_EMPTY_COURSE_NAME",
                -1,
                -1,
                new long[0],
                sessionId,
                "THE_EMPTY_YEAR"
        );
    }

    public Exam commit(String courseName, int term, int semester, long[] versions, String year) {
        this.courseName = courseName;
        this.term = term;
        this.semester = semester;
        this.versions=versions;
        this.year=year;
        return this;
    }
}
