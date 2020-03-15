package com.example.examscanner.components.scan_exam.capture;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModel;

import java.util.LinkedList;
import java.util.Queue;


public class CaptureViewModel extends ViewModel {
    private MutableLiveData<Integer> mNumOfTotalCaptures;
    private Queue<Capture> unProcessedCaptures;
    private CornerDetectionViewModel cornerDetectionViewModel;


    public CaptureViewModel(CornerDetectionViewModel cornerDetectionViewModel) {
        this.cornerDetectionViewModel= cornerDetectionViewModel;
        unProcessedCaptures = new LinkedList<>();
        mNumOfTotalCaptures = new MutableLiveData<>(unProcessedCaptures.size());

    }


    public LiveData<Integer> getNumOfTotalCaptures() {
        return mNumOfTotalCaptures;
    }
    public LiveData<Integer> getNumOfProcessedCaptures() {return cornerDetectionViewModel.getNumberOfCornerDetectedCaptures();}
    public void consumeCapture(Capture capture){
        unProcessedCaptures.add(capture);
        mNumOfTotalCaptures.setValue(
                unProcessedCaptures.size()+cornerDetectionViewModel.getNumberOfCornerDetectedCaptures().getValue()
        );
    }

    public void processCapture(){
        cornerDetectionViewModel.detectCorners(unProcessedCaptures.remove());

    }

    public void postProcessCapture(){
        cornerDetectionViewModel.postProcessCornerDetection();
    }



}
