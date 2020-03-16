package com.example.examscanner.components.scan_exam.detect_corners;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.components.scan_exam.capture.Capture;
import com.example.examscanner.components.scan_exam.capture.CornerDetectedCapture;
import com.example.examscanner.components.scan_exam.reslove_answers.ResolveAnswersViewModel;
import com.example.examscanner.image_processing.ICornerDetectionResult;
import com.example.examscanner.image_processing.ImageProcessingFacade;

import java.util.ArrayList;
import java.util.List;


public class CornerDetectionViewModel extends ViewModel {
    private List<MutableLiveData<CornerDetectedCapture>> cornerDetectedCaptures;
    private MutableLiveData<Integer> mNumberOfCornerDetectedCaptures;
    private ImageProcessingFacade imageProcessor;
    private ResolveAnswersViewModel resolveAnswersViewModel;

    public CornerDetectionViewModel(ImageProcessingFacade imageProcessor, ResolveAnswersViewModel resolveAnswersViewModel) {
        this.resolveAnswersViewModel =resolveAnswersViewModel;
        this.imageProcessor=imageProcessor;
        this.cornerDetectedCaptures = new ArrayList<>();
        mNumberOfCornerDetectedCaptures = new MutableLiveData<>(this.cornerDetectedCaptures.size());
    }

    public LiveData<Integer> getNumberOfCornerDetectedCaptures() {
        return mNumberOfCornerDetectedCaptures;
    }

    public LiveData<Integer> getNumberOfAnswersScannedCaptures() {
        return resolveAnswersViewModel.getNumberOfAnswersScannedCaptures();
    }

    public void detectCorners(Capture capture) {
        ICornerDetectionResult result = imageProcessor.detectCorners(capture);
        cornerDetectedCaptures.add(
                new MutableLiveData<>(new CornerDetectedCapture(result.getBitmap()))
        );
    }

    public void postProcessCornerDetection() {
        mNumberOfCornerDetectedCaptures.setValue(mNumberOfCornerDetectedCaptures.getValue()+1);
    }

//    public void processCornerDetectedCapture(CornerDetectedCapture cornerDetectedCapture){
    public void transformAndScanAnswers(){
        CornerDetectedCapture currCapture =cornerDetectedCaptures.get(0).getValue();
        currCapture.setBitmap(
                imageProcessor.transformToRectangle(
                        currCapture.getBitmap(),
                        currCapture.getUpperLeft(),
                        currCapture.getUpperRight(),
                        currCapture.getBottomRight(),
                        currCapture.getBottomLeft()
                )
        );
        resolveAnswersViewModel.scanAnswers(currCapture.getBitmap());
    }

    public Bitmap tmpGetCurrBitmap(){
        return cornerDetectedCaptures.get(0).getValue().getBitmap();
    }

    public void postProcessTransformAndScanAnswers() {
        resolveAnswersViewModel.postProcessScanAnswers();
    }

    public List<MutableLiveData<CornerDetectedCapture>> getCornerDetectedCaptures() {
        return cornerDetectedCaptures;
    }

    public MutableLiveData<CornerDetectedCapture> getCornerDetectedCaptureById(int captureId) {
        return cornerDetectedCaptures.get(captureId);
    }
}
