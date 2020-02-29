package com.example.examscanner.communication;

import org.json.JSONArray;
import org.json.JSONObject;

public class RealFacadeImple extends FacadeImpl {
    private static RealFacadeImple instance;
    public static RealFacadeImple getInstance(){
        if (instance==null){
            instance = new RealFacadeImple();
            return instance;
        }else{
            return instance;
        }
    }
    @Override
    public JSONObject getExam(int id) {
        return null;
    }

    @Override
    public JSONObject getVersion(int id) {
        return null;
    }

    @Override
    public JSONArray getExams() {
        return null;
    }

    @Override
    public JSONObject getGrader(int id) {
        return null;
    }
}
