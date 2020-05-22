package com.example.examscanner.persistence.remote.entities;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class Exam {

    public static String metaManager = "manager";
    public static String metaGraders = "graders";
    public static String metaCourseName = "courseName";
    public static String metaSemester = "semester";
    public static String metaTerm = "term";
    public static String metaYear = "year";
    public static String metaSeal = "seal";
    public static String metaUrl = "url";
    public String manager;
    public List<String> graders;
    public String courseName;
    public int semester;
    public int term;
    public String year;
    public boolean seal = false;
    public String url;
    private String id;

    public Exam() {
    }

    public Exam(String manager, List<String> graders, String courseName, int semester, int term, String year, boolean seal, String url) {
        this.manager = manager;
        this.graders = graders;
        this.courseName = courseName;
        this.semester = semester;
        this.term = term;
        this.year = year;
        this.seal = seal;
        this.url = url;
    }

    public String _getId() {
        return id;
    }
    public void  _setId(String theId) {
        id= theId;
    }
}
