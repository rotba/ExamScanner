package com.example.examscanner.repositories.exam;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
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

public class ExamConverter implements Converter<ExamEntityInterface, Exam> {
    private Repository<Version> vRepo = new VersionRepoFactory().create();
    private Repository<Grader> gRepo = new GraderRepoFactory().create();

    @Override
    public Exam convert(final ExamEntityInterface examEntityInterface) {
        return new Exam(
                examEntityInterface.getID(),
                null,
                new ArrayList<>(),
                examEntityInterface.getCourseName(),
                examEntityInterface.getTerm(),
                examEntityInterface.getSemester(),
                examEntityInterface.getVersionsIds(),
                examEntityInterface.getSessionId(),
                examEntityInterface.getYear()
        );
    }
}
