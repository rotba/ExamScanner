package com.example.examscanner.components.scan_exam.reslove_answers;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModel;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModelFactory;
import com.example.examscanner.image_processing.ImageProcessingFactory;

public class ResolveAnswersViewModelFactory implements ViewModelProvider.Factory {
    FragmentActivity fragmentActivity;

    public ResolveAnswersViewModelFactory(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) ViewModelProviders.of(
                        fragmentActivity,
                        new InitialFactory()
                ).get(ResolveAnswersViewModel.class);
    }

    private class InitialFactory implements ViewModelProvider.Factory{
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ResolveAnswersViewModel(
                    new ImageProcessingFactory().create()
            );
        }
    }
}
