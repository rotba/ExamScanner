package com.example.examscanner.components.scan_exam.detect_corners;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.examscanner.components.scan_exam.capture.CaptureViewModel;

public class CornerDetectionViewModelFactory implements ViewModelProvider.Factory {
    FragmentActivity fragment;

    public CornerDetectionViewModelFactory(FragmentActivity fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CornerDetectionViewModel(
                ViewModelProviders.of(fragment).get(CaptureViewModel.class)
        );
    }
}
