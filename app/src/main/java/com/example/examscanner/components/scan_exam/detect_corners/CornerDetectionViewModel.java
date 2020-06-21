package com.example.examscanner.components.scan_exam.detect_corners;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
    private static int QAD_counter = (int)(Math.random()*1000);
    private List<MutableLiveData<ScannedCapture>> scannedCaptures;
    private MutableLiveData<Integer> mNumberOfCornerDetectedCaptures;
    private MutableLiveData<Integer> mNumberOfAnswersScannedCaptures;
    private ImageProcessingFacade imageProcessor;
//    private Repository<CornerDetectedCapture> cdcRepo;
    private Repository<ScannedCapture> scRepo;
    private List<Long> thisSessionProcessedCaptures;
    private String FOR_DEBUGGING_resultOfScanAnswers;
    private Exam exam;


    public CornerDetectionViewModel(ImageProcessingFacade imageProcessor, Repository<ScannedCapture> scRepo , Exam exam) {
//        this.cdcRepo = cdcRepo;
        this.scRepo = scRepo;
        this.imageProcessor = imageProcessor;
        this.exam = exam;
        scannedCaptures = new ArrayList<>();
//        for (ScannedCapture sc : this.scRepo.get(c -> c.isAssocaitedWith(exam))) {
//            scannedCaptures.add(new MutableLiveData<ScannedCapture>(sc));
//        }
        mNumberOfCornerDetectedCaptures = new MutableLiveData<>(this.scannedCaptures.size());
        mNumberOfAnswersScannedCaptures = new MutableLiveData<>(0);
        thisSessionProcessedCaptures = new ArrayList<Long>();
    }

    public void refresh(){
        scannedCaptures = new ArrayList<>();
        for (ScannedCapture sc : this.scRepo.get(c -> c.isAssocaitedWith(exam))) {
            scannedCaptures.add(new MutableLiveData<ScannedCapture>(sc));
        }
    }

//    public LiveData<Integer> getNumberOfCDCaptures() {
//        return mNumberOfCornerDetectedCaptures;
//    }
//
//    public LiveData<Integer> getNumberOfScannedCaptures() {
//        return mNumberOfAnswersScannedCaptures;
//    }

//    public void align(CornerDetectedCapture cdc) {
//        cdc.setBitmap(
//                imageProcessor.align(
//                        cdc.getBitmap(),
//                        cdc.getVersion().getPerfectImage()
//                )
//        );
//    }

//    public void transformToRectangle(CornerDetectedCapture cdc) {
//        cdc.setBitmap(
//                imageProcessor.transformToRectangle(
//                        cdc.getBitmap(),
//                        cdc.getUpperLeft(),
//                        cdc.getUpperRight(),
//                        cdc.getBottomRight(),
//                        cdc.getBottomLeft()
//                )
//        );
//    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void scanAnswers(CornerDetectedCapture cdc){
//        ScanAnswersConsumer consumer = new ScanAnswersConsumer() {
//            @Override
//            public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
//                final ScannedCapture t = new ScannedCapture(scRepo.genId(), cdc.getBitmap(), exam.getNumOfQuestions(), numOfAnswersDetected, answersIds, lefts, tops, rights, bottoms, selections,cdc.getVersion(), NOT_SUPPORTING_EXAMINEE_ID_EXTRACTION_YET+String.valueOf(QAD_counter++));
//                FOR_DEBUGGING_resultOfScanAnswers = t.toString();
//                scRepo.create(t);
//            }
//        };
//        float[] leftMostXs = cdc.getVersion().getRealtiveLefts();
////                .getQuestions().stream().mapToInt(q -> q.getLeft()).toArray();
//        float[] upperMostYs = cdc.getVersion().getRealtiveUps();
////                .getQuestions().stream().mapToInt(q -> q.getUp()).toArray();
//        imageProcessor.scanAnswers(cdc.getBitmap(), exam.getNumOfQuestions(), consumer, leftMostXs, upperMostYs);
////        imageProcessor.scanAnswers(cdc.getBitmap(), exam.getNumOfQuestions(), consumer);
//    }


//    public void postProcessTransformAndScanAnswers(long captureId) {
//        thisSessionProcessedCaptures.add(captureId);
//        mNumberOfAnswersScannedCaptures.setValue(mNumberOfAnswersScannedCaptures.getValue()+1);
//    }

    public List<MutableLiveData<ScannedCapture>> getPreProcessedCDCs() {
        List<MutableLiveData<ScannedCapture>> ans = new ArrayList<>();
        for (MutableLiveData<ScannedCapture> sc: scannedCaptures) {
            if(!thisSessionProcessedCaptures.contains(sc.getValue().getId()))
                ans.add(sc);
        }
        return ans;
    }

    public MutableLiveData<ScannedCapture> getScannedCaptureById(long captureId) {
        for (MutableLiveData<ScannedCapture> sc : scannedCaptures) {
            if(sc.getValue().getId()==captureId) return sc;
        }
        throw new CornerDetectionUseCaseException(String.format("Can't find CDC with id: %d", captureId));
    }

//    public void setVersion(long captureId, int verNum){
//        CornerDetectedCapture cdc = getScannedCaptureById(captureId).getValue();
//        cdc.setVersion(exam.getVersionByNum(verNum));
//        getScannedCaptureById(captureId).setValue(cdc);
//    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public int[] getVersionNumbers() {
//        return exam.getVersions().stream().mapToInt(Version::getNum).toArray();
//    }

    public List<MutableLiveData<ScannedCapture>> getSCs() {
        return scannedCaptures;
    }

    public String getFOR_DEBUGGING_resultOfScanAnswers() {
        return FOR_DEBUGGING_resultOfScanAnswers;
    }

    public void setFOR_DEBUGGING_resultOfScanAnswers(String FOR_DEBUGGING_resultOfScanAnswers) {
        this.FOR_DEBUGGING_resultOfScanAnswers = FOR_DEBUGGING_resultOfScanAnswers;
    }

    public void remove(ScannedCapture sc) {
        scRepo.removeFromCache(sc.getId());
    }

    public void delete(ScannedCapture sc) {
        scRepo.delete(sc.getId());
    }

    public long getExamId() {
        return exam.getId();
    }

    public void commitResolutions(long scanId) {
        ScannedCapture scannedCapture = getScannedCaptureById(scanId).getValue();
        scannedCapture.commitResolutions();
        scannedCapture.setBitmap(
                imageProcessor.createFeedbackImage(scannedCapture.getBm(), scannedCapture.getRelLefts(), scannedCapture.getRelTops(), scannedCapture.getSelections(), scannedCapture.getIds(), scannedCapture.getExamineeId())
        );
        getScannedCaptureById(scanId).postValue(scannedCapture);
        scRepo.update(scannedCapture);
    }

    public void approve(ScannedCapture sc) {
        sc.approve();
    }
}
