package com.example.examscanner.components.scan_exam.capture;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;

import java.util.LinkedList;
import java.util.Queue;


public class CaptureViewModel extends ViewModel {
//    private MutableLiveData<String> mText;
    private MutableLiveData<Integer> mNumOfTotalCaptures;
    private MutableLiveData<Integer> mNumOfProcessedCaptures;
    private Queue<Capture> unProcessedCaptures;
    private Queue<Object> processedCaptures;
    private ImageProcessingFacade imageProcessor = new ImageProcessingFactory().create();


    public CaptureViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is Scan fragment");
        unProcessedCaptures = new LinkedList<>();
        processedCaptures = new LinkedList<>();
        mNumOfProcessedCaptures = new MutableLiveData<>(processedCaptures.size());
        mNumOfTotalCaptures = new MutableLiveData<>(unProcessedCaptures.size()+processedCaptures.size());

    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void init() {
//        if(mCaptures!=null){
//            return;
//        }else{
//            mCaptures = new MutableLiveData<Queue<Capture>>(new LinkedList<>());
//        }
//        if(mProcessedCaptures!=null){
//            return;
//        }else{
//            mProcessedCaptures = new MutableLiveData<>(0);
//        }
//    }

//    public LiveData<String> getText() {
//        return mText;
//    }
    public LiveData<Integer> getNumOfTotalCaptures() {
        return mNumOfTotalCaptures;
    }
    public LiveData<Integer> getNumOfProcessedCaptures() {return mNumOfProcessedCaptures;}
    public void consumeCapture(Capture capture){
        unProcessedCaptures.add(capture);
        mNumOfTotalCaptures.setValue(unProcessedCaptures.size()+ processedCaptures.size());
    }

    public void processCapture(){
        processedCaptures.add(imageProcessor.foo(unProcessedCaptures.remove()));
    }

    public void postProcessCapture(){
        mNumOfProcessedCaptures.setValue(mNumOfProcessedCaptures.getValue()+1);
    }
}
