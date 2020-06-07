package com.example.examscanner.persistence.remote.entities;

import java.util.HashMap;
import java.util.Map;

public class ExamineeSolution {
    public static String metaAnswers = "answers";
    public static String metaVersionId = "versionId";
    public static Object metaGrade = "grade";
    public Map<String, Integer> answers;
    public String versionId;
    public Float grade;
    private String id;
    public ExamineeSolution(String versionId, Map<String, Integer> answers) {
        this.answers = answers;
        this.versionId = versionId;
    }

    public String _getId(){
        return id;
    }
    public void _setId(String id){
        this.id=id;
    }
}
