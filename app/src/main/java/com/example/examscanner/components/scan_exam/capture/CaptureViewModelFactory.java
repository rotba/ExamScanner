package com.example.examscanner.components.scan_exam.capture;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class CaptureViewModelFactory implements ViewModelProvider.Factory {
    FragmentActivity activity;
    public CaptureViewModelFactory(FragmentActivity activity) {
        this.activity=activity;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)ViewModelProviders.of(activity).get(CaptureViewModel.class);
    }
}
