package com.example.examscanner.components.scan_exam.scan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScanViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ScanViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Scan fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
