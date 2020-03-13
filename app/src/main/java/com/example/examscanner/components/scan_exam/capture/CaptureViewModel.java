package com.example.examscanner.components.scan_exam.capture;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.examscanner.image_processing.ICornerDetectionResult;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class CaptureViewModel extends ViewModel {
//    private MutableLiveData<String> mText;
    private MutableLiveData<Integer> mNumOfTotalCaptures;
    private MutableLiveData<Integer> mNumOfProcessedCaptures;
    private Queue<Capture> unProcessedCaptures;
    private Queue<CornerDetectedCapture> processedCaptures;
    private ImageProcessingFacade imageProcessor = new ImageProcessingFactory().create();


    public CaptureViewModel() {
        unProcessedCaptures = new LinkedList<>();
        processedCaptures = new LinkedList<>();
        mNumOfProcessedCaptures = new MutableLiveData<>(processedCaptures.size());
        mNumOfTotalCaptures = new MutableLiveData<>(unProcessedCaptures.size()+processedCaptures.size());

    }


    public LiveData<Integer> getNumOfTotalCaptures() {
        return mNumOfTotalCaptures;
    }
    public LiveData<Integer> getNumOfProcessedCaptures() {return mNumOfProcessedCaptures;}
    public void consumeCapture(Capture capture){
        unProcessedCaptures.add(capture);
        mNumOfTotalCaptures.setValue(unProcessedCaptures.size()+ processedCaptures.size());
    }

    public void processCapture(){
        ICornerDetectionResult result = imageProcessor.detectCorners(unProcessedCaptures.remove());
        processedCaptures.add(
                new CornerDetectedCapture(result.getBitmap())
        );
    }

    public void postProcessCapture(){
        mNumOfProcessedCaptures.setValue(mNumOfProcessedCaptures.getValue()+1);
    }


    public List<CornerDetectedCapture> getCornerDetectedCaptures(){
        List<CornerDetectedCapture> ans = new LinkedList<>();
        for (int i = 0; i <processedCaptures.size() ; i++) {
            CornerDetectedCapture pc =  processedCaptures.remove();
            ans.add(pc);
            processedCaptures.add(pc);
        }
        return ans;
    }
}
