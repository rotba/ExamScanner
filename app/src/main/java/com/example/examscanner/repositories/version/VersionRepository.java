package com.example.examscanner.repositories.version;

import com.example.examscanner.repositories.Converter;
import com.example.examscanner.repositories.Repository;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class VersionRepository implements Repository<Version> {
    private Converter<JSONObject, Version> converter  = new VersionConverter();

    private static VersionRepository instance;
    public static VersionRepository getInstance(){
        if (instance==null){
            instance = new VersionRepository();
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
    public Version get(long id) {
        return null;
    }

    @Override
    public List<Version> get(Predicate<Version> criteria) {
        return new ArrayList<>();
    }

    @Override
    public void create(Version version) {

    }

    @Override
    public void update(Version version) {

    }

    @Override
    public void delete(int id) {

    }
    @Override
    public int genId() {
        return -1;
    }
}
