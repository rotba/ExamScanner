package com.example.examscanner.components.scan_exam.detect_corners;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.examscanner.image_processing.ImageProcessingFactory;

public class CornerDetectionViewModelFactory implements ViewModelProvider.Factory {
    FragmentActivity activity;

    public CornerDetectionViewModelFactory(FragmentActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) ViewModelProviders.of(activity, new InitialFactory()).get(CornerDetectionViewModel.class);
    }

    private class InitialFactory implements ViewModelProvider.Factory{
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T)new CornerDetectionViewModel(new ImageProcessingFactory().create());
        }
    }
}
