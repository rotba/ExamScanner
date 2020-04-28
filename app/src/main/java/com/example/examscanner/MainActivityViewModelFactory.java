package com.example.examscanner;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.authentication.UICalimentAuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.StateFactory;

public class MainActivityViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new MainActivityViewModel(
                StateFactory.get(),
                UICalimentAuthenticationHandlerFactory.get()
        );
    }
}
