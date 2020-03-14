package com.example.examscanner.components.scan_exam.detect_corners;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.components.scan_exam.capture.CaptureViewModel;
import com.example.examscanner.components.scan_exam.capture.CornerDetectedCapture;

import java.util.List;

public class CornerDetectionViewModel extends ViewModel {
    private CaptureViewModel captureViewModel;
    private List<CornerDetectedCapture> cornerDetectedCaptures;
    private MutableLiveData<Integer> mNumberOfTotalCaptures;

    public CornerDetectionViewModel(CaptureViewModel captureViewModel) {
        this.captureViewModel = captureViewModel;
        this.cornerDetectedCaptures = captureViewModel.getCornerDetectedCaptures();
        mNumberOfTotalCaptures = new MutableLiveData<>(this.cornerDetectedCaptures.size());
    }

    public LiveData<Integer> getNumberOfTotalCaptures() {
        return mNumberOfTotalCaptures;
    }
    public ProcessedCapture getProcessedCapture(){
        return new ProcessedCapture();
    }
}
