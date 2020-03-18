package com.example.examscanner.components.scan_exam.detect_corners;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCaptureRepositoryFacrory;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;

public class CornerDetectionViewModelFactory implements ViewModelProvider.Factory {
    FragmentActivity activity;

    public CornerDetectionViewModelFactory(FragmentActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new CornerDetectionViewModel(
                new ImageProcessingFactory().create(),
                new CornerDetectedCaptureRepositoryFacrory().create(),
                new ScannedCaptureRepositoryFactory().create()
        );
    }

//    private class InitialFactory implements ViewModelProvider.Factory{
//        @NonNull
//        @Override
//        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//            return (T)new CornerDetectionViewModel(
//                    new ImageProcessingFactory().create(),
//                    new ResolveAnswersViewModelFactory(activity)
//                            .create(ResolveAnswersViewModel.class)
//            );
//        }
//    }
}
