package com.example.examscanner.repositories.version;

import com.example.examscanner.repositories.Converter;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class VersionRepository implements Repository<Version> {
    private Converter<JSONObject, Version> converter  = new VersionConverter();
    private Repository<Exam> examRepository;

    private static VersionRepository instance;
    public static VersionRepository getInstance(){
        if (instance==null){
//            instance = new VersionRepository(new ExamRepositoryFactory().create());
            instance = new VersionRepository(null);
            return instance;
        }else{
            return instance;
        }
    }

    public VersionRepository(Repository<Exam> examRepository) {
        this.examRepository = examRepository;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Version get(long id) {
        return new Version(
                null,
                null,
                id
        );
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
