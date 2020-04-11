package com.example.examscanner.repositories.version;

import com.example.examscanner.communication.CommunicationFacade;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;
import com.example.examscanner.repositories.Converter;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class VersionRepository implements Repository<Version> {
    private CommunicationFacade comFacade;
    private Converter<VersionEntityInterface, Version> converter;

    private static VersionRepository instance;
    public static VersionRepository getInstance(){
        if (instance==null){
            instance = new VersionRepository(new CommunicationFacadeFactory().create(), new VersionConverter());
            return instance;
        }else{
            return instance;
        }
    }

    public VersionRepository(CommunicationFacade comFacade, Converter<VersionEntityInterface, Version> converter) {
        this.comFacade = comFacade;
        this.converter = converter;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Version get(long id) {
        return converter.convert(comFacade.getVersionById(id));
    }

    @Override
    public List<Version> get(Predicate<Version> criteria) {
        return new ArrayList<>();
    }

    @Override
    public void create(Version version) {
        long verId = comFacade.createVersion(version.getExam().getId(), version.getNum());
        version.setId(verId);
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
