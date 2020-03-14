package com.example.examscanner.components.scan_exam.reslove_answers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModel;

import java.util.jar.Attributes;

public class ResolveAnswersViewModel extends ViewModel {
    private CornerDetectionViewModel cornerDetectionViewModel;
    private MutableLiveData<Integer> mNumOfIdentified;
    private MutableLiveData<Integer> mNumOfUnidentified;

    public ResolveAnswersViewModel(CornerDetectionViewModel cornerDetectionViewModel) {
        this.cornerDetectionViewModel = cornerDetectionViewModel;
        mNumOfIdentified = new MutableLiveData<>(this.cornerDetectionViewModel.getProcessedCapture().getNumberOfIdentified());
        mNumOfUnidentified = new MutableLiveData<>(this.cornerDetectionViewModel.getProcessedCapture().getNumberOfUnidentified());
    }

    public LiveData<Integer> getNumOfIdentified() {
        return mNumOfIdentified;
    }

    public LiveData<Integer> getNumOfUnidentified() {
        return mNumOfUnidentified;
    }
}
