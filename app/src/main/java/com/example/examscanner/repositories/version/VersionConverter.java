package com.example.examscanner.repositories.version;

import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;
import com.example.examscanner.repositories.Converter;

import org.json.JSONException;
import org.json.JSONObject;

public class VersionConverter implements Converter<VersionEntityInterface, Version> {


    @Override
    public Version convert(VersionEntityInterface ei) {
        return new Version(
                ei.getId(),
                ei.getNumber(),
                ei.getExamId(),
                ei.getQuestions()
        );
    }
}
