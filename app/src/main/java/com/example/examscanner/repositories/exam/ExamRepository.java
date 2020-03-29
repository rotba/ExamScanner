package com.example.examscanner.repositories.exam;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.examscanner.communication.CommunicationFacade;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.repositories.Converter;
import com.example.examscanner.repositories.Repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class ExamRepository implements Repository<Exam> {
    private CommunicationFacade comFacade = new CommunicationFacadeFactory().create();
    private Converter<JSONObject, Exam> converter = new ExamConverter();
    private MapHandler mapper = new MapHandler(converter);
    private FilterHandler filterrer = new FilterHandler();
    private static ExamRepository instance;
    private static final String TAG = "ExamRepository";
    private int currAvailableId = 0;

    public static ExamRepository getInstance(){
        if (instance==null){
            instance = new ExamRepository();
            return instance;
        }else{
            return instance;
        }
    }
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Exam get(int id) {
        try {
            return converter.convert(
                    comFacade.getExam(id)
            );
        }catch (JSONException e){
            Log.v(TAG, "Can't find exam with an id: " + id);
            return null;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Exam> get(Predicate<Exam> criteria) {
        try {
            return filterrer.filter(
                    mapper.map(comFacade.getExams()),
                    criteria
            );
        } catch (JSONException e) {
            Log.v(
                    TAG,
                    "Unexpected error retrieving exams.\n Error description:"+e.toString()
            );
            return null;
        }

    }

    @Override
    public void create(Exam exam) {

    }

    @Override
    public void update(Exam exam) {

    }

    @Override
    public void delete(int id) {

    }

    private class MapHandler {
        private Converter<JSONObject, Exam> converter;

        public MapHandler(Converter<JSONObject, Exam> converter) {
            this.converter = converter;
        }

        public List<Exam> map(JSONArray array) throws JSONException {
            List<Exam> ans = new ArrayList<>();
            for (int i = 0; i <array.length() ; i++) {
                ans.add(converter.convert(array.getJSONObject(i)));
            }
            return ans;
        }
    }

    private class FilterHandler {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public List<Exam> filter(List<Exam> exams, Predicate<Exam> p){
            List<Exam> ans = new ArrayList<>();
            for (Exam e: exams) {
                if(p.test(e)){
                    ans.add(e);
                }
            }
            return ans;
        }
    }
    @Override
    public int genId() {
        int ans = currAvailableId;
        currAvailableId++;
        return ans;
    }
}
