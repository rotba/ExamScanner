package com.example.examscanner.persistence.remote.entities;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class Exam {

    public String managaerId;
    public List<String> graders;
    public String courseName;
    public int semester;
    public int term;
    public String year;

    public Exam() {
    }

    public Exam(String managaerId, List<String> graders, String courseName, int semester, int term, String year) {
        this.managaerId = managaerId;
        this.graders = graders;
        this.courseName = courseName;
        this.semester = semester;
        this.term = term;
        this.year = year;
    }
}
