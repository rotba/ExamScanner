package com.example.examscanner.components.scan_exam.reslove_answers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;

public class ResolveAnswersViewModelFactory implements ViewModelProvider.Factory {
    FragmentActivity fragmentActivity;
    ImageProcessingFacade imageProcessor = new ImageProcessingFactory().create();
    Repository<ScannedCapture>repo = new ScannedCaptureRepositoryFactory().create();

    public ResolveAnswersViewModelFactory(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

//    public ResolveAnswersViewModelFactory(FragmentActivity activity, ImageProcessingFacade imageProcessor) {
//        this.fragmentActivity = activity;
//        this.imageProcessor = imageProcessor;
//    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ResolveAnswersViewModel(
                imageProcessor,
                repo
        );
    }
}
