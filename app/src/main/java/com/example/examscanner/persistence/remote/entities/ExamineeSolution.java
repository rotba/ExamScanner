package com.example.examscanner.persistence.remote.entities;

import java.util.HashMap;
import java.util.Map;

public class ExamineeSolution {
    public static String metaAnswers = "answers";
    public static String metaVersionId = "versionId";
    public static Object metaGrade = "grade";
    public Map<Integer, Integer> answers;
    public String versionId;
    public Float grade;
    private String id;
    public String examineeId;

    public ExamineeSolution(String versionId, Map<Integer, Integer> answers) {
        this.examineeId = examineeId;
        this.versionId = versionId;
        this.grade = grade;
        this.answers = answers;
    }

    public ExamineeSolution(String examineeId, String versionId, float grade, Map<Integer, Integer> answers) {
        this.examineeId = examineeId;
        this.versionId = versionId;
        this.grade = grade;
        this.answers = answers;
    }

    public String _getId(){
        return id;
    }
    public void _setId(String id){
        this.id=id;
    }
}