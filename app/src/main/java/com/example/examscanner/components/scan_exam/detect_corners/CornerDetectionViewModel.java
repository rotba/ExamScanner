package com.example.examscanner.components.scan_exam.detect_corners;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.repositories.version.Version;

import java.util.ArrayList;
import java.util.List;


public class CornerDetectionViewModel extends ViewModel {
    private List<MutableLiveData<CornerDetectedCapture>> cornerDetectedCaptures;
    private MutableLiveData<Integer> mNumberOfCornerDetectedCaptures;
    private MutableLiveData<Integer> mNumberOfAnswersScannedCaptures;
    private ImageProcessingFacade imageProcessor;
    private Repository<Version> verRepo;
    private Repository<CornerDetectedCapture> cdcRepo;
    private Repository<ScannedCapture> scRepo;
    private List<Long> thisSessionProcessedCaptures;
    private Exam exam;


    public CornerDetectionViewModel(ImageProcessingFacade imageProcessor, Repository<CornerDetectedCapture> cdcRepo, Repository<ScannedCapture> scRepo, Repository<Version> verRepo , Exam exam) {
        this.cdcRepo = cdcRepo;
        this.scRepo = scRepo;
        this.imageProcessor = imageProcessor;
        this.verRepo = verRepo;
        cornerDetectedCaptures = new ArrayList<>();
        for (CornerDetectedCapture cdc : this.cdcRepo.get(c -> true)) {
            cornerDetectedCaptures.add(new MutableLiveData<CornerDetectedCapture>(cdc));
        }
        mNumberOfCornerDetectedCaptures = new MutableLiveData<>(this.cornerDetectedCaptures.size());
        mNumberOfAnswersScannedCaptures = new MutableLiveData<>(0);
        this.exam = exam;
        thisSessionProcessedCaptures = new ArrayList<Long>();
    }

    public LiveData<Integer> getNumberOfCDCaptures() {
        return mNumberOfCornerDetectedCaptures;
    }

    public LiveData<Integer> getNumberOfScannedCaptures() {
        return mNumberOfAnswersScannedCaptures;
    }

    public void transformToRectangle(CornerDetectedCapture cdc) {
        cdc.setBitmap(
                imageProcessor.transformToRectangle(
                        cdc.getBitmap(),
                        cdc.getUpperLeft(),
                        cdc.getUpperRight(),
                        cdc.getBottomRight(),
                        cdc.getBottomLeft()
                )
        );
    }

    public void scanAnswers(CornerDetectedCapture cdc){
        ScanAnswersConsumer consumer = new ScanAnswersConsumer() {
            @Override
            public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                scRepo.create(new ScannedCapture(scRepo.genId(),cdc.getBitmap(),53/*TODO - get version*/,numOfAnswersDetected, answersIds, lefts, tops, rights, bottoms, selections));
            }
        };
        imageProcessor.scanAnswers(cdc.getBitmap(), 53, consumer);
    }


    public void postProcessTransformAndScanAnswers(long captureId) {
        thisSessionProcessedCaptures.add(captureId);
        mNumberOfAnswersScannedCaptures.setValue(scRepo.get(sc -> true).size());
    }

    public List<MutableLiveData<CornerDetectedCapture>> getPreProcessedCDCs() {
        List<MutableLiveData<CornerDetectedCapture>> ans = new ArrayList<>();
        for (MutableLiveData<CornerDetectedCapture> cdc:cornerDetectedCaptures) {
            if(!thisSessionProcessedCaptures.contains(cdc.getValue().getId()))
                ans.add(cdc);
        }
        return ans;
    }

    public MutableLiveData<CornerDetectedCapture> getCDCById(long captureId) {
        for (MutableLiveData<CornerDetectedCapture> cdc :cornerDetectedCaptures) {
            if(cdc.getValue().getId()==captureId) return cdc;
        }
        throw new CornerDetectionUseCaseException(String.format("Can't find CDC with id: %d", captureId));
    }

    public void setVersion(long captureId, int verNum){
        CornerDetectedCapture cdc = getCDCById(captureId).getValue();
        cdc.setVersionId(verRepo.get(v->v.getExamId()==exam.getId() && v.getNum()==verNum).get(0).getId());
        getCDCById(captureId).setValue(cdc);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int[] getVersionNumbers() {
        return verRepo.get(v->v.getExamId()==exam.getId()).stream().mapToInt(Version::getNum).toArray();
    }

    public List<MutableLiveData<CornerDetectedCapture>>getCDCs() {
        return cornerDetectedCaptures;
    }
}
