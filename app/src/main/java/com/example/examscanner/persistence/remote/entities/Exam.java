package com.example.examscanner.persistence.remote.entities;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class Exam {

    public String manager;
    public List<String> graders;
    public String courseName;
    public int semester;
    public int term;
    public String year;
    public boolean seal = false;

    public Exam() {
    }

    public Exam(String manager, List<String> graders, String courseName, int semester, int term, String year, boolean seal) {
        this.manager = manager;
        this.graders = graders;
        this.courseName = courseName;
        this.semester = semester;
        this.term = term;
        this.year = year;
        this.seal = seal;
    }
}
