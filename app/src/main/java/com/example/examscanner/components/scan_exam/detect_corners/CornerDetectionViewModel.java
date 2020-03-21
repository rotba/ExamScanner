package com.example.examscanner.components.scan_exam.detect_corners;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
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
    private Exam exam;


    public CornerDetectionViewModel(ImageProcessingFacade imageProcessor, Repository<CornerDetectedCapture> cdcRepo, Repository<ScannedCapture> scRepo, Exam exam) {
        this.cdcRepo = cdcRepo;
        this.scRepo = scRepo;
        this.imageProcessor = imageProcessor;
        cornerDetectedCaptures = new ArrayList<>();
        for (CornerDetectedCapture cdc : this.cdcRepo.get(c -> true)) {
            cornerDetectedCaptures.add(new MutableLiveData<CornerDetectedCapture>(cdc));
        }
        mNumberOfCornerDetectedCaptures = new MutableLiveData<>(this.cornerDetectedCaptures.size());
        mNumberOfAnswersScannedCaptures = new MutableLiveData<>(0);
        this.exam = exam;
    }

    public LiveData<Integer> getNumberOfCornerDetectedCaptures() {
        return mNumberOfCornerDetectedCaptures;
    }

    public LiveData<Integer> getNumberOfAnswersScannedCaptures() {
        return mNumberOfAnswersScannedCaptures;
    }


    public void postProcessCornerDetection() {
        mNumberOfCornerDetectedCaptures.setValue(mNumberOfCornerDetectedCaptures.getValue() + 1);
    }

    public void transformToRectangle(CornerDetectedCapture cdc) {
        cdc.setBitmap(
                imageProcessor.transformToRectangle(
                        cdc.getBitmap(),
                        cdc.getUpperLeft(),
                        cdc.getUpperRight(),
                        cdc.getBottomRight(),
                        cdc.getBottomLeft()
                )
        );
    }

    public void scanAnswers(CornerDetectedCapture cdc){
        ScanAnswersConsumer consumer = new ScanAnswersConsumer() {
            @Override
            public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                scRepo.create(new ScannedCapture(cdc.getBitmap(),exam.getNumOfAnswers(),numOfAnswersDetected, answersIds, lefts, tops, rights, bottoms, selections));
            }
        };
        imageProcessor.scanAnswers(cdc.getBitmap(), exam.getNumOfAnswers(), consumer);
    }


    public void postProcessTransformAndScanAnswers() {
        mNumberOfAnswersScannedCaptures.setValue(scRepo.get(sc -> true).size());
    }

    public List<MutableLiveData<CornerDetectedCapture>> getCornerDetectedCaptures() {
        return cornerDetectedCaptures;
    }

    public MutableLiveData<CornerDetectedCapture> getCornerDetectedCaptureById(int captureId) {
        for (MutableLiveData<CornerDetectedCapture> cdc :cornerDetectedCaptures) {
            if(cdc.getValue().getId()==captureId) return cdc;
        }
        return null;
    }
}
