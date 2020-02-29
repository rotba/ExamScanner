package com.example.examscanner.communication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

public class FacadeImplProxy extends FacadeImpl {
    private RealFacadeImple realImpl = RealFacadeImple.getInstance();
    @Override
    public JSONObject getExam(int id) {
        if (useReal()){
            return realImpl.getExam(id);
        }
        else {
            try {
                return new JSONObject()
                        .put("id", "0")
                        .put("manager", "0")
                        .put("graders", new JSONArray("[0]"))
                        .put("course_name", "COMP")
                        .put("moed", "A");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public JSONObject getVersion(int id) {
        if (useReal()){
            return realImpl.getExam(id);
        }
        else {
            String someAnwersString = "[1,2,3,2,1,3,4,5,3,3,3,3,1,1,1]";
            try {
                return new JSONObject()
                        .put("exam_id", "0")
                        .put("answers", new JSONArray(someAnwersString));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public JSONObject getGrader(int id) {
        return null;
    }

    private boolean useReal(){return false;}

    @Override
    public JSONArray getExams() {
        JSONArray ans =  new JSONArray();
        ans.put(getExam(0));
        return ans;
    }
}
