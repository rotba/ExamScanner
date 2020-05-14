package com.example.examscanner.components.scan_exam.capture;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.stubs.BitmapInstancesFactory;

public class CaptureViewModelFactory implements ViewModelProvider.Factory {
    FragmentActivity activity;
    private long sessionId;

    public CaptureViewModelFactory(FragmentActivity activity , long sessionId, long examId) {
        this.activity=activity;
        if(sessionId == -1){
            this.sessionId = new CommunicationFacadeFactory().create().createNewScanExamSession(examId);
        }else{
            this.sessionId = sessionId;
        }

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CaptureViewModel(
                new CDCRepositoryFacrory().create(),
                new ImageProcessingFactory().create(),
                sessionId
        );
    }
}
