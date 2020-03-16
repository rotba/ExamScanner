package com.example.examscanner.components.scan_exam.reslove_answers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;

public class ResolveAnswersViewModelFactory implements ViewModelProvider.Factory {
    FragmentActivity fragmentActivity;
    ImageProcessingFacade imageProcessor;

    public ResolveAnswersViewModelFactory(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        this.imageProcessor = new ImageProcessingFactory().create();
    }

    public ResolveAnswersViewModelFactory(FragmentActivity activity, ImageProcessingFacade imageProcessor) {
        this.fragmentActivity = activity;
        this.imageProcessor = imageProcessor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ViewModelProvider(
                fragmentActivity,
                new InitialFactory()
        ).get(ResolveAnswersViewModel.class);
    }

    private class InitialFactory implements ViewModelProvider.Factory {
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ResolveAnswersViewModel(
                    imageProcessor
            );
        }
    }
}
