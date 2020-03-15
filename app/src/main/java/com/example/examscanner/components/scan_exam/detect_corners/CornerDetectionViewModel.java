package com.example.examscanner.components.scan_exam.detect_corners;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.components.scan_exam.capture.Capture;
import com.example.examscanner.components.scan_exam.capture.CornerDetectedCapture;
import com.example.examscanner.image_processing.ICornerDetectionResult;
import com.example.examscanner.image_processing.ImageProcessingFacade;

import java.util.ArrayList;
import java.util.List;

public class CornerDetectionViewModel extends ViewModel {
    private List<CornerDetectedCapture> cornerDetectedCaptures;
    private MutableLiveData<Integer> mNumberOfCornerDetectedCaptures;
    private ImageProcessingFacade imageProcessor;

    public CornerDetectionViewModel(ImageProcessingFacade imageProcessor) {
        this.imageProcessor=imageProcessor;
        this.cornerDetectedCaptures = new ArrayList<>();
        mNumberOfCornerDetectedCaptures = new MutableLiveData<>(this.cornerDetectedCaptures.size());
    }

    public LiveData<Integer> getNumberOfCornerDetectedCaptures() {
        return mNumberOfCornerDetectedCaptures;
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
}
