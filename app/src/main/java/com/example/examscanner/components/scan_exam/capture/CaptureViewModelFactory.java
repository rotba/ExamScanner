package com.example.examscanner.components.scan_exam.capture;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCaptureRepositoryFacrory;

public class CaptureViewModelFactory implements ViewModelProvider.Factory {
    FragmentActivity activity;
    public CaptureViewModelFactory(FragmentActivity activity) {
        this.activity=activity;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CaptureViewModel(
                new CornerDetectedCaptureRepositoryFacrory().create(),
                new ImageProcessingFactory().create()
        );
    }
}
