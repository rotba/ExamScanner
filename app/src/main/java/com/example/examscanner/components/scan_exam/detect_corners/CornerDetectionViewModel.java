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
import com.example.examscanner.repositories.exam.Version;

import java.util.ArrayList;
import java.util.List;


public class CornerDetectionViewModel extends ViewModel {
    private static final String NOT_SUPPORTING_EXAMINEE_ID_EXTRACTION_YET = "NOT_SUPPORTING_EXAMINEE_ID_EXTRACTION_YET";
    private List<MutableLiveData<CornerDetectedCapture>> cornerDetectedCaptures;
    private MutableLiveData<Integer> mNumberOfCornerDetectedCaptures;
    private MutableLiveData<Integer> mNumberOfAnswersScannedCaptures;
    private ImageProcessingFacade imageProcessor;
    private Repository<CornerDetectedCapture> cdcRepo;
    private Repository<ScannedCapture> scRepo;
    private List<Long> thisSessionProcessedCaptures;
    private String FOR_DEBUGGING_resultOfScanAnswers;
    private Exam exam;


    public CornerDetectionViewModel(ImageProcessingFacade imageProcessor, Repository<CornerDetectedCapture> cdcRepo, Repository<ScannedCapture> scRepo , Exam exam) {
        this.cdcRepo = cdcRepo;
        this.scRepo = scRepo;
        this.imageProcessor = imageProcessor;
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

    public void align(CornerDetectedCapture cdc) {
        cdc.setBitmap(
                imageProcessor.align(
                        cdc.getBitmap(),
                        cdc.getVersion().getPerfectImage()
                )
        );
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void scanAnswers(CornerDetectedCapture cdc){
        ScanAnswersConsumer consumer = new ScanAnswersConsumer() {
            @Override
            public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                final ScannedCapture t = new ScannedCapture(scRepo.genId(), cdc.getBitmap(), exam.getNumOfQuestions(), numOfAnswersDetected, answersIds, lefts, tops, rights, bottoms, selections,cdc.getVersion(), NOT_SUPPORTING_EXAMINEE_ID_EXTRACTION_YET);
                FOR_DEBUGGING_resultOfScanAnswers = t.toString();
                scRepo.create(t);
            }
        };
        float[] leftMostXs = cdc.getVersion().getRealtiveLefts();
//                .getQuestions().stream().mapToInt(q -> q.getLeft()).toArray();
        float[] upperMostYs = cdc.getVersion().getRealtiveUps();
//                .getQuestions().stream().mapToInt(q -> q.getUp()).toArray();
        imageProcessor.scanAnswers(cdc.getBitmap(), exam.getNumOfQuestions(), consumer, leftMostXs, upperMostYs);
//        imageProcessor.scanAnswers(cdc.getBitmap(), exam.getNumOfQuestions(), consumer);
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
        cdc.setVersion(exam.getVersionByNum(verNum));
        getCDCById(captureId).setValue(cdc);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int[] getVersionNumbers() {
        return exam.getVersions().stream().mapToInt(Version::getNum).toArray();
    }

    public List<MutableLiveData<CornerDetectedCapture>>getCDCs() {
        return cornerDetectedCaptures;
    }

    public String getFOR_DEBUGGING_resultOfScanAnswers() {
        return FOR_DEBUGGING_resultOfScanAnswers;
    }

    public void setFOR_DEBUGGING_resultOfScanAnswers(String FOR_DEBUGGING_resultOfScanAnswers) {
        this.FOR_DEBUGGING_resultOfScanAnswers = FOR_DEBUGGING_resultOfScanAnswers;
    }
}
