package com.example.examscanner.components.scan_exam.capture;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Queue;

public class CaptureViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    private Queue<Capture> imCaptures;

    public CaptureViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Scan fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public void processCapture(Capture capture){
    }
}
