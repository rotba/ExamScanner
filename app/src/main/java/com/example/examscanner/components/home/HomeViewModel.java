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


import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class HomeViewModel extends ViewModel {

    private Repository<Exam> examRepository;

    public HomeViewModel(Repository<Exam> examRepository) {
         this.examRepository = examRepository;
    }

    public List<LiveData<Exam>> getExams() {
        List<LiveData<Exam>>  mExams = new ArrayList<>();
        for (Exam e :examRepository.get(e->true)) {
            mExams.add(new MutableLiveData<>(e));
        }
        return mExams;
    }
}