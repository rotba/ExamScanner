package com.example.examscanner.repositories.exam;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.examscanner.communication.CommunicationFacade;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.repositories.Converter;
import com.example.examscanner.repositories.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class ExamRepository implements Repository<Exam> {
    private CommunicationFacade comFacade = new CommunicationFacadeFactory().create();
    private Converter<ExamEntityInterface, Exam> converter = new ExamConverter();

    private static ExamRepository instance;
    private static final String TAG = "ExamRepository";
    private int currAvailableId = 0;

    public static ExamRepository getInstance() {
        if (instance == null) {
            instance = new ExamRepository();
            return instance;
        } else {
            return instance;
        }
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Exam get(long id) {
        return converter.convert(comFacade.getExamById(id));
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Exam> get(Predicate<Exam> criteria) {
        List<Exam> ans = new ArrayList<>();
        for (ExamEntityInterface ei : comFacade.getExams()) {
            Exam e = converter.convert(ei);
            if (criteria.test(converter.convert(ei)))
                ans.add(e);
        }
        return ans;
    }

    @Override
    public void create(Exam exam) {
        long id= comFacade.createExam(
                exam.getCourseName(),
                exam.getURL(),
                exam.getYear(),
                exam.getTerm(),
                exam.getSemester(),
                exam.getSessionId()
        );
        exam.setId(id);
    }

    @Override
    public void update(Exam exam) {
        comFacade.updateExam(
                exam.getId(),
                exam.getCourseName(),
                exam.getSemester(),
                exam.getTerm(),
                exam.getVersions(),
                exam.getSessionId(),
                exam.getYear());
    }

    @Override
    public void delete(int id) {

    }


    @Override
    public int genId() {
        int ans = currAvailableId;
        currAvailableId++;
        return ans;
    }
}
