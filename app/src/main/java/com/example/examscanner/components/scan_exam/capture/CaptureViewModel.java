package com.example.examscanner.components.scan_exam.capture;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModel;
import com.example.examscanner.image_processing.ICornerDetectionResult;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;


public class CaptureViewModel extends ViewModel {
    private MutableLiveData<Integer> mNumOfTotalCaptures;
    private MutableLiveData<Integer> mNumOfProcessedCaptures;
    private Queue<Capture> unProcessedCaptures;
    private Repository<CornerDetectedCapture> cdcRepo;
    private ImageProcessingFacade imageProcessor;



    public CaptureViewModel(Repository<CornerDetectedCapture> cdcRepo, ImageProcessingFacade imageProcessor) {
        unProcessedCaptures = new LinkedList<>();
        mNumOfTotalCaptures = new MutableLiveData<>(unProcessedCaptures.size());
        this.cdcRepo=cdcRepo;
        this.imageProcessor= imageProcessor;
        mNumOfProcessedCaptures = new MutableLiveData<>(cdcRepo.get(cornerDetectedCapture -> true).size());

    }


    public LiveData<Integer> getNumOfTotalCaptures() {
        return mNumOfTotalCaptures;
    }
    public LiveData<Integer> getNumOfProcessedCaptures() {return mNumOfProcessedCaptures;}
    public void consumeCapture(Capture capture){
        unProcessedCaptures.add(capture);
        mNumOfTotalCaptures.setValue(
                unProcessedCaptures.size()+mNumOfProcessedCaptures.getValue()
        );
    }

    public void processCapture(){
        cdcRepo.create(
                convert(imageProcessor.detectCorners(unProcessedCaptures.remove()))
        );
    }

    public void postProcessCapture(){
        mNumOfProcessedCaptures.setValue(cdcRepo.get(cornerDetectedCapture -> true).size());
    }

    public CornerDetectedCapture convert(ICornerDetectionResult icdr){
        return new CornerDetectedCapture(icdr.getBitmap());
    }






}
