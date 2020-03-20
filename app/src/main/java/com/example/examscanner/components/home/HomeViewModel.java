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


import java.util.List;
import java.util.function.Predicate;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Exam>> mExams;
    private Repository<Exam> examRepository = new ExamRepositoryFactory().create();

    public LiveData<List<Exam>> getExams() {
        return mExams;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void init() {
        if(mExams!=null){
            return;
        }else{
            mExams = new MutableLiveData<>();
            final int currentGraderId = State.getState().getGraderId();
            mExams.setValue(examRepository.get(new Predicate<Exam>() {
                @Override
                public boolean test(Exam exam) {
                    return exam.associatedWithGrader(currentGraderId);
                }
            }));
        }
    }
}