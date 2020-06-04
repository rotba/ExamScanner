package com.example.examscanner.components.scan_exam.capture;

import android.graphics.PointF;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.Version;

import java.util.LinkedList;
import java.util.Queue;


public class CaptureViewModel extends ViewModel {
    private MutableLiveData<Integer> mNumOfTotalCaptures;
    private MutableLiveData<Integer> mNumOfProcessedCaptures;
    private Queue<Capture> unProcessedCaptures;
    private Repository<CornerDetectedCapture> cdcRepo;
    private MutableLiveData<Version> mVersion;
    private MutableLiveData<String> mExamineeId;
    private ImageProcessingFacade imageProcessor;
    private long sessionId;
    private Exam exam;


    public CaptureViewModel(Repository<CornerDetectedCapture> cdcRepo, ImageProcessingFacade imageProcessor, long sessionId, Exam exam) {
        this.exam = exam;
        unProcessedCaptures = new LinkedList<>();
        this.cdcRepo = cdcRepo;
        this.imageProcessor = imageProcessor;
        mNumOfProcessedCaptures = new MutableLiveData<>(cdcRepo.get(cornerDetectedCapture -> cornerDetectedCapture.getSession()==sessionId).size());
        mNumOfTotalCaptures = new MutableLiveData<>(mNumOfProcessedCaptures.getValue());
        mVersion = new MutableLiveData<>();
        mExamineeId = new MutableLiveData<>();
        this.sessionId = sessionId;

    }


    public LiveData<Integer> getNumOfTotalCaptures() {
        return mNumOfTotalCaptures;
    }

    public LiveData<Integer> getNumOfProcessedCaptures() {
        return mNumOfProcessedCaptures;
    }

    public void consumeCapture(Capture capture) {
        unProcessedCaptures.add(capture);
        mNumOfTotalCaptures.setValue(mNumOfTotalCaptures.getValue()+1);
    }

    public void processCapture() {
        Capture capture = unProcessedCaptures.remove();
        imageProcessor.detectCorners(
                capture.getBitmap(),
                new DetectCornersConsumer() {
                    @Override
                    public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                        cdcRepo.create(
                                new CornerDetectedCapture(
                                        capture.getBitmap(), upperLeft, upperRight,bottomRight,bottomLeft, sessionId
                                )
                        );
                    }
                }
        );
    }

    public void postProcessCapture() {
        mNumOfProcessedCaptures.setValue(cdcRepo.get(cornerDetectedCapture -> cornerDetectedCapture.getSession()==sessionId).size());
    }

    public MutableLiveData<String> getCurrentExamineeId() {
        return mExamineeId;
    }

    public LiveData<Version> getCurrentVersion() {
        return mVersion;
    }

    public boolean isValidVersion() {
        return mVersion.getValue()!=null ;
    }

    public boolean isValidExamineeId() {
        return mExamineeId.getValue()!=null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int[] getVersionNumbers() {
        return exam.getVersions().stream().mapToInt(Version::getNum).toArray();
    }

    public void setVersion(Integer intChoice) {
        mVersion.setValue(exam.getVersionByNum(intChoice));
    }

    public void setExamineeId(String toString) {
        mExamineeId.setValue(toString);
    }
}
