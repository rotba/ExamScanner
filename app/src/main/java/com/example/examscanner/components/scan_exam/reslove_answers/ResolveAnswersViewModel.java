package com.example.examscanner.components.scan_exam.reslove_answers;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModel;
import com.example.examscanner.image_processing.IScannedCapture;
import com.example.examscanner.image_processing.ImageProcessingFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class ResolveAnswersViewModel extends ViewModel {
    private MutableLiveData<Integer> mNumOfIdentified;
    private MutableLiveData<Integer> mNumOfUnidentified;
    private MutableLiveData<Integer> mNumOfAnswersScannedAnswers;
    private List<ScannedCapture> scannedCaptures;
    private ImageProcessingFacade imageProcessor;

    public ResolveAnswersViewModel(ImageProcessingFacade imageProcessor) {
        this.imageProcessor=imageProcessor;
        mNumOfIdentified = new MutableLiveData<>(0);
        mNumOfUnidentified = new MutableLiveData<>(0);
        mNumOfAnswersScannedAnswers = new MutableLiveData<>(0);
        scannedCaptures = new ArrayList<>();
    }

    private int i =0;
    public LiveData<Integer> getNumOfIdentified() {
        i++;
        if(scannedCaptures.size()>0){
            mNumOfIdentified.setValue(scannedCaptures.size());
        }
        return mNumOfIdentified;
    }

    public LiveData<Integer> getNumOfUnidentified() {
        return mNumOfUnidentified;
    }

    public void scanAnswers(Bitmap bitmap) {
        IScannedCapture iScannedCapture = imageProcessor.scanAnswers(bitmap);
        scannedCaptures.add(
                new ScannedCapture(
                        iScannedCapture.getIdentified(),
                        iScannedCapture.getUnidentified(),
                        iScannedCapture.getAnswers()
                )
        );
    }

    public void postProcessScanAnswers(){
        mNumOfIdentified.setValue(scannedCaptures.get(0).getIdentified());
        mNumOfUnidentified.setValue(scannedCaptures.get(0).getUnidentified());
        mNumOfAnswersScannedAnswers.setValue(mNumOfAnswersScannedAnswers.getValue()+1);
    }

    public LiveData<Integer> getNumberOfAnswersScannedCaptures() {
        return mNumOfAnswersScannedAnswers;
    }
}
