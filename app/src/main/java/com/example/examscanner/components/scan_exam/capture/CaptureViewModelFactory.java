package com.example.examscanner.components.scan_exam.capture;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCaptureRepositoryFacrory;
import com.example.examscanner.repositories.version.VersionRepoFactory;
import com.example.examscanner.stubs.BitmapInstancesFactory;

public class CaptureViewModelFactory implements ViewModelProvider.Factory {
    FragmentActivity activity;
    private long versionId;
    private long sessionId;

    public CaptureViewModelFactory(FragmentActivity activity, long versionId, long sessionId) {
        this.activity=activity;
        this.versionId = versionId;
        this.sessionId = sessionId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CaptureViewModel(
                new CornerDetectedCaptureRepositoryFacrory().create(),
                new VersionRepoFactory().create(),
                new ImageProcessingFactory(new BitmapInstancesFactory(activity)).create(),
                versionId,
                sessionId
        );
    }
}
