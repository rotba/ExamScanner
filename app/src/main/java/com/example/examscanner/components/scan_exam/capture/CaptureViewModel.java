package com.example.examscanner.components.scan_exam.capture;

import android.graphics.PointF;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.version.Version;

import java.util.LinkedList;
import java.util.Queue;


public class CaptureViewModel extends ViewModel {
    private MutableLiveData<Integer> mNumOfTotalCaptures;
    private MutableLiveData<Integer> mNumOfProcessedCaptures;
    private Queue<Capture> unProcessedCaptures;
    private Repository<CornerDetectedCapture> cdcRepo;
    private ImageProcessingFacade imageProcessor;
    private Repository<Version> verRepo;
    private long versionId;
    private long sessionId;


    public CaptureViewModel(Repository<CornerDetectedCapture> cdcRepo, Repository<Version> verRepo, ImageProcessingFacade imageProcessor, long versionId, long sessionId) {
        this.verRepo = verRepo;
        this.versionId = versionId;
        unProcessedCaptures = new LinkedList<>();
        mNumOfTotalCaptures = new MutableLiveData<>(unProcessedCaptures.size());
        this.cdcRepo = cdcRepo;
        this.imageProcessor = imageProcessor;
        mNumOfProcessedCaptures = new MutableLiveData<>(cdcRepo.get(cornerDetectedCapture -> true).size());

    }


    public LiveData<Integer> getNumOfTotalCaptures() {
        return mNumOfTotalCaptures;
    }

    public LiveData<Integer> getNumOfProcessedCaptures() {
        return mNumOfProcessedCaptures;
    }

    public void consumeCapture(Capture capture) {
        unProcessedCaptures.add(capture);
        mNumOfTotalCaptures.setValue(
                unProcessedCaptures.size() + mNumOfProcessedCaptures.getValue()
        );
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
                                        capture.getBitmap(), upperLeft, upperRight, bottomLeft, bottomRight, /*TODO - to long*/verRepo.get((int) versionId), sessionId
                                )
                        );
                    }
                }
        );

    }

    public void postProcessCapture() {
        mNumOfProcessedCaptures.setValue(cdcRepo.get(cornerDetectedCapture -> true).size());
    }

    public long getVersionId() {
        return versionId;
    }
}
