package com.example.examscanner.repositories.version;

import com.example.examscanner.repositories.Converter;

import org.json.JSONException;
import org.json.JSONObject;

public class VersionConverter implements Converter<JSONObject, Version> {
    @Override
    public Version convert(JSONObject jsonObject) throws JSONException {
        return null;
    }
}
