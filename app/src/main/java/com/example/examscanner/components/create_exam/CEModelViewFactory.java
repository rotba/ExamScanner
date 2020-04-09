package com.example.examscanner.components.create_exam;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ApplicationProvider;

import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.question.QuestionRepositoryFactory;
import com.example.examscanner.repositories.session.CreateExamSessionProviderFactory;
import com.example.examscanner.repositories.session.ScanExamSessionProviderFactory;
import com.example.examscanner.repositories.version.VersionRepoFactory;
import com.example.examscanner.stubs.BitmapInstancesFactory;

public class CEModelViewFactory implements ViewModelProvider.Factory {
    private FragmentActivity activity;

    public CEModelViewFactory(FragmentActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new CreateExamModelView(
                new ExamRepositoryFactory().create(),
                new VersionRepoFactory().create(),
                new QuestionRepositoryFactory().create(),
                new ImageProcessingFactory(new BitmapInstancesFactory(ApplicationProvider.getApplicationContext())).create(),
                new CreateExamSessionProviderFactory().create().provide()
        );
    }
}
