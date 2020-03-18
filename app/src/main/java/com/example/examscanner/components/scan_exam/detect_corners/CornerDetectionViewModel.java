package com.example.examscanner.components.scan_exam.detect_corners;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.components.scan_exam.capture.Capture;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.image_processing.IScannedCapture;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.image_processing.ICornerDetectionResult;
import com.example.examscanner.image_processing.ImageProcessingFacade;

import java.util.ArrayList;
import java.util.List;


public class CornerDetectionViewModel extends ViewModel {
    private List<MutableLiveData<CornerDetectedCapture>> cornerDetectedCaptures;
    private MutableLiveData<Integer> mNumberOfCornerDetectedCaptures;
    private MutableLiveData<Integer> mNumberOfAnswersScannedCaptures;
    private ImageProcessingFacade imageProcessor;
    private Repository<CornerDetectedCapture> cdcRepo;
    private Repository<ScannedCapture> scRepo;


    public CornerDetectionViewModel(ImageProcessingFacade imageProcessor, Repository<CornerDetectedCapture> cdcRepo, Repository<ScannedCapture> scRepo) {
        this.cdcRepo =cdcRepo;
        this.scRepo = scRepo;
        this.imageProcessor=imageProcessor;
        cornerDetectedCaptures = new ArrayList<>();
        for (CornerDetectedCapture cdc:this.cdcRepo.get(c->true)) {
            cornerDetectedCaptures.add(new MutableLiveData<CornerDetectedCapture>(cdc));
        }
        mNumberOfCornerDetectedCaptures = new MutableLiveData<>(this.cornerDetectedCaptures.size());
        mNumberOfAnswersScannedCaptures = new MutableLiveData<>(0);
    }

    public LiveData<Integer> getNumberOfCornerDetectedCaptures() {
        return mNumberOfCornerDetectedCaptures;
    }

    public LiveData<Integer> getNumberOfAnswersScannedCaptures() {
        return mNumberOfAnswersScannedCaptures;
    }

//    public void detectCorners(Capture capture) {
//        ICornerDetectionResult result = imageProcessor.detectCorners(capture);
//        cornerDetectedCaptures.add(
//                new MutableLiveData<>(new CornerDetectedCapture(result.getBitmap()))
//        );
//    }

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
        scRepo.create(convert(imageProcessor.scanAnswers(currCapture.getBitmap())));
    }

    private ScannedCapture convert(IScannedCapture scanAnswers) {
        return new ScannedCapture(scanAnswers.getIdentified(),scanAnswers.getUnidentified(),scanAnswers.getAnswers());
    }

    public Bitmap tmpGetCurrBitmap(){
        return cornerDetectedCaptures.get(0).getValue().getBitmap();
    }

    public void postProcessTransformAndScanAnswers() {
        mNumberOfAnswersScannedCaptures.setValue(scRepo.get(sc->true).size());
    }

    public List<MutableLiveData<CornerDetectedCapture>> getCornerDetectedCaptures() {
        return cornerDetectedCaptures;
    }

    public MutableLiveData<CornerDetectedCapture> getCornerDetectedCaptureById(int captureId) {
        return cornerDetectedCaptures.get(captureId);
    }
}
