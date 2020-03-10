package com.example.examscanner.components.scan_exam.capture;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.State;
import com.example.examscanner.repositories.exam.Exam;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;

public class CaptureViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    private MutableLiveData<Queue<Capture>> mCaptures;

    public CaptureViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Scan fragment");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void init() {
        if(mCaptures!=null){
            return;
        }else{
            mCaptures = new MutableLiveData<Queue<Capture>>(new LinkedList<>());
        }
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<Queue<Capture>> getCaptures() {
        return mCaptures;
    }
    public void processCapture(Capture capture){
        Queue q = mCaptures.getValue();
        q.add(capture);
        mCaptures.setValue(q);
    }
}
