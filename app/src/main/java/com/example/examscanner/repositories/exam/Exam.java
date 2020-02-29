package com.example.examscanner.repositories.exam;

import androidx.annotation.NonNull;

import com.example.examscanner.repositories.grader.ExamManager;
import com.example.examscanner.repositories.grader.Grader;
import com.example.examscanner.repositories.version.Version;

import java.util.List;

public class Exam {
    private ExamManager manager;
    private int id;
    private String courseName;
    private String moed;
    private List<Version> versions;
    private List<Grader> graders;

    public Exam(int id, ExamManager manager , List<Grader> graders,String courseName, String moed, List<Version> versions) {
        this.courseName = courseName;
        this.moed = moed;
        this.versions = versions;
        this.manager=manager;
        this.graders=graders;
    }



    public int getId() {
        return id;
    }

    public boolean associatedWithGrader(int currentGraderId) {
        for (Grader g:graders) {
            if (g.getId() == currentGraderId) return true;
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return courseName+" "+ moed;
    }
}
