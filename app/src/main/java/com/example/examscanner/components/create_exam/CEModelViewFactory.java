package com.example.examscanner.components.create_exam;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.session.SessionProviderFactory;
import com.example.examscanner.repositories.version.VersionRepoFactory;

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
                new SessionProviderFactory().create().provide("Create Exam")
        );
    }
}
