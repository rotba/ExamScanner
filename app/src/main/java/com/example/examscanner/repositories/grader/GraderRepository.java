package com.example.examscanner.repositories.grader;

import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.communication.Facade;
import com.example.examscanner.repositories.Repository;

import java.util.List;
import java.util.function.Predicate;

public class GraderRepository implements Repository<Grader> {
    private Facade comFacade = new CommunicationFacadeFactory().create();
    private static GraderRepository instance;
    private static final String TAG = "GraderRepository";

    public static GraderRepository getInstance(){
        if (instance==null){
            instance = new GraderRepository();
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
    public Grader get(int id) {
        //TODO - implement comFacade get Grader
        return new ExamManager(id);
    }

    @Override
    public List<Grader> get(Predicate<Grader> criteria) {
        return null;
    }

    @Override
    public void create(Grader grader) {

    }

    @Override
    public void update(Grader grader) {

    }

    @Override
    public void delete(int id) {

    }
    @Override
    public int genId() {
        return -1;
    }
}
