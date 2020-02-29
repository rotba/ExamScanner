package com.example.examscanner.communication;

import com.example.examscanner.repositories.grader.Grader;

import org.json.JSONArray;
import org.json.JSONObject;

public interface Facade {
    public JSONObject getExam(int id);
    public JSONObject getVersion(int id);
    public JSONArray getExams();
    public JSONObject getGrader(int id);
}
