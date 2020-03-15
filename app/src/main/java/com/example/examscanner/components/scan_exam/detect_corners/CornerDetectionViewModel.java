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
    private List<CornerDetectedCapture> cornerDetectedCaptures;
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

    public ProcessedCapture getProcessedCapture(){
        return new ProcessedCapture();
    }

    public void detectCorners(Capture capture) {
        ICornerDetectionResult result = imageProcessor.detectCorners(capture);
        cornerDetectedCaptures.add(
                new CornerDetectedCapture(result.getBitmap())
        );
    }

    public void postProcessCornerDetection() {
        mNumberOfCornerDetectedCaptures.setValue(mNumberOfCornerDetectedCaptures.getValue()+1);
    }

//    public void processCornerDetectedCapture(CornerDetectedCapture cornerDetectedCapture){
    public void transformAndScanAnswers(){
        cornerDetectedCaptures.get(0).setBitmap(
                imageProcessor.transformToRectangle(
                        cornerDetectedCaptures.get(0).getBitmap(),
                        cornerDetectedCaptures.get(0).getUpperLeft(),
                        cornerDetectedCaptures.get(0).getUpperRight(),
                        cornerDetectedCaptures.get(0).getBottomRight(),
                        cornerDetectedCaptures.get(0).getBottomLeft()
                )
        );
        resolveAnswersViewModel.scanAnswers(cornerDetectedCaptures.get(0).getBitmap());
    }

    public Bitmap tmpGetCurrBitmap(){
        return cornerDetectedCaptures.get(0).getBitmap();
    }

    public void postProcessTransformAndScanAnswers() {
        resolveAnswersViewModel.postProcessScanAnswers();
    }
}
