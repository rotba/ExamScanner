package com.example.examscanner.repositories.exam;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.examscanner.repositories.Converter;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.grader.ExamManager;
import com.example.examscanner.repositories.grader.Grader;
import com.example.examscanner.repositories.grader.GraderRepoFactory;
import com.example.examscanner.repositories.version.Version;
import com.example.examscanner.repositories.version.VersionRepoFactory;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ExamConverter implements Converter<JSONObject, Exam> {
    private Repository<Version> vRepo = new VersionRepoFactory().create();
    private Repository<Grader> gRepo = new GraderRepoFactory().create();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Exam convert(final JSONObject jsonObject) throws JSONException {
        try {
            final int examId = jsonObject.getInt("id");
            final int examManagerId = jsonObject.getInt("manager");
            return new Exam(
                    examId,
                    //TODO - need to figure out a way to deal with extensions relations in db. This is areally bad solution
                    (ExamManager)gRepo.get(examManagerId),
                    //TODO - need to figure out a way to deal with many2many relations in db. This is areally bad solution
                    mapToGraders(jsonObject.getJSONArray("graders")),
                    jsonObject.getString("course_name"),
                    jsonObject.getString("moed"),
                    vRepo.get(new Predicate<Version>() {
                        @Override
                        public boolean test(Version version) {
                            return version.isAssociatedWith(examId);
                        }
                    })
            );
        } catch (JSONException e){
            throw e;
        }
    }

    private List<Grader> mapToGraders(JSONArray jGraders) throws JSONException {
        List<Grader> ans = new ArrayList<>();
        for (int i = 0; i < jGraders.length(); i++) {
            try {
                ans.add(gRepo.get(jGraders.getInt(i)));
            } catch (JSONException e) {
                throw e;
            }
        }
        return ans;
    }
}
