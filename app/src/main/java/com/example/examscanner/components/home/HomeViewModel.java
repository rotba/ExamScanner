package com.example.examscanner.components.home;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.State;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.session.ScanExamSession;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class HomeViewModel extends ViewModel {

    private Repository<Exam> examRepository;
    private Repository<ScanExamSession> sesRepo;

    public HomeViewModel(Repository<Exam> examRepository, Repository<ScanExamSession> sesRepo) {
         this.examRepository = examRepository;
        this.sesRepo = sesRepo;
    }

    public List<LiveData<Exam>> getExams() {
        List<LiveData<Exam>>  mExams = new ArrayList<>();
        for (Exam e :examRepository.get(e->true)) {
            mExams.add(new MutableLiveData<>(e));
        }
        return mExams;
    }

    public long getLastSession(long examId) {
        List<ScanExamSession> ses = sesRepo.get(s->s.examId()==examId);
        if(ses.size()==0)return -1;
        long last = -1;
        for (ScanExamSession s:ses) {
            last = Math.max(last,s.getId());
        }
        return last;
    }
}