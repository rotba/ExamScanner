package com.example.examscanner.components.scan_exam.capture;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.Version;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.reactivex.schedulers.Schedulers;


public class CaptureViewModel extends ViewModel {
    private final String REMOVE_INDICATION = "REMOVE:";
    private final String TAG = "ExamScanner";
    private MutableLiveData<Integer> mNumOfTotalCaptures;
    private MutableLiveData<Integer> mNumOfProcessedCaptures;
    private Queue<Capture> unProcessedCaptures;
    private Repository<ScannedCapture> scRepo;
    private MutableLiveData<Version> mVersion;
    private MutableLiveData<String> mExamineeId;
    private ImageProcessingFacade imageProcessor;
    private List<String> examineeIds;
    private long sessionId;
    private Exam exam;
    private int[] versionNumers;
    private boolean thereAreScannedCaptures;
    private boolean aBoolean;


    public CaptureViewModel(Repository<ScannedCapture> scRepo, ImageProcessingFacade imageProcessor, long sessionId, Exam exam) {
        this.exam = exam;
        unProcessedCaptures = new LinkedList<>();
        this.scRepo = scRepo;
        this.imageProcessor = imageProcessor;
//        mNumOfProcessedCaptures = new MutableLiveData<>(scRepo.get(sc -> sc.getSession() == sessionId).size());
//        mNumOfProcessedCaptures = new MutableLiveData<>(scRepo.get(sc -> sc.isAssocaitedWith(this.exam)).size());
        mNumOfProcessedCaptures = new MutableLiveData<>(0);
        mNumOfTotalCaptures = new MutableLiveData<>(mNumOfProcessedCaptures.getValue());
        mVersion = new MutableLiveData<>();
        mExamineeId = new MutableLiveData<>();
        this.sessionId = sessionId;
        examineeIds = new ArrayList<>();
        exam.observeExamineeIds()
        .subscribeOn(Schedulers.io())
        .subscribe(this::consumeExamineeId);
        thereAreScannedCaptures = !scRepo.get(sc->true).isEmpty();
    }

    private void consumeExamineeId(String s) {
        synchronized (examineeIds){
            if(s.indexOf(REMOVE_INDICATION)!=-1){
                String sRemove =s.replace(REMOVE_INDICATION,"");
                if(!examineeIds.contains(sRemove)){
                    Log.d(TAG, String.format("BUG in examinee ids removal. dont want to crach the app: s:%s, sRemove:%s",s,sRemove));
                }else{
                    examineeIds.remove(sRemove);
                }
            }else{
                examineeIds.add(cannonic(s));
            }
        }
    }

    public void refresh(){
        thereAreScannedCaptures = !scRepo.get(sc->true).isEmpty();
        mNumOfProcessedCaptures = new MutableLiveData<>(0);
        mNumOfTotalCaptures = new MutableLiveData<>(mNumOfProcessedCaptures.getValue());
    }


    public LiveData<Integer> getNumOfTotalCaptures() {
        return mNumOfTotalCaptures;
    }

    public LiveData<Integer> getNumOfProcessedCaptures() {
        return mNumOfProcessedCaptures;
    }

    public void consumeCapture(Capture capture) {
        unProcessedCaptures.add(capture);
        mNumOfTotalCaptures.postValue(mNumOfTotalCaptures.getValue() + 1);
    }

    public void processCapture() {
        Capture capture = unProcessedCaptures.remove();
        final Version version = capture.getVersion();
        capture.setBitmap(imageProcessor.align(capture.getBitmap(), version.getPerfectImage()));
        imageProcessor.scanAnswers(
                capture.getBitmap(),
                exam.getNumOfQuestions(),
                new ScanAnswersConsumer() {
                    @Override
                    public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                        final Bitmap bitmap = imageProcessor.createFeedbackImage(capture.getBitmap(), lefts, tops,selections,answersIds);
                        Log.d(TAG, "starting creating ScannedCapture");
                        scRepo.create(new ScannedCapture(
                                -1, bitmap,capture.getBitmap(), exam.getNumOfQuestions(), numOfAnswersDetected, answersIds, lefts, tops, rights, bottoms, selections, version, capture.getExamineeId()

                        ));
                    }
                },
                version.getRealtiveLefts(),
                version.getRealtiveUps()
        );
//        imageProcessor.detectCorners(
//                capture.getBitmap(),
//                new DetectCornersConsumer() {
//                    @Override
//                    public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
//                        cdcRepo.create(
//                                new CornerDetectedCapture(
//                                        capture.getBitmap(), upperLeft, upperRight,bottomRight,bottomLeft, sessionId
//                                )
//                        );
//                    }
//                }
//        );
    }

    public void postProcessCapture() {
//        mNumOfProcessedCaptures.setValue(scRepo.get(sc -> sc.getSession() == sessionId).size());
        mVersion.postValue(null);
        mExamineeId.postValue(null);
        mNumOfProcessedCaptures.postValue(mNumOfProcessedCaptures.getValue()+1);
    }

    public MutableLiveData<String> getCurrentExamineeId() {
        return mExamineeId;
    }

    public LiveData<Version> getCurrentVersion() {
        return mVersion;
    }

    public boolean isValidVersion() {
        aBoolean = mVersion.getValue() != null;
        if(!aBoolean){
            Log.d(TAG, String.format("in valid version :%s", mVersion.getValue()));
        }
        return aBoolean;
    }

    public boolean isValidExamineeId() {
        if(mExamineeId.getValue() == null){
            Log.d(TAG, String.format("in valid examineeId :%s", mExamineeId.getValue()));
            return false;
        }
        if(mExamineeId.getValue().equals("")){
            Log.d(TAG, String.format("in valid examineeId :%s", mExamineeId.getValue()));
            return false;
        }
        return true;
    }

    public boolean isHoldingValidExamineeId() {
        return mExamineeId.getValue() != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int[] getVersionNumbers() {
        if(versionNumers == null){
            versionNumers = exam.getVersions().stream().mapToInt(Version::getNum).toArray();
        }
        return versionNumers;
    }

    public void setVersion(Integer intChoice) {
        try {
            mVersion.setValue(exam.getVersionByNum(intChoice));
        }catch (Exception e){
            Log.d(TAG, "mVersion.setValue(exam.getVersionByNum(intChoice)) failed probably because of main thread stuff", e);
            mVersion.postValue(exam.getVersionByNum(intChoice));
        }
    }

    public void setExamineeId(String toString) {
        try {
            mExamineeId.setValue(toString);
        }catch (Exception e){
            Log.d(TAG, "mExamineeId.setValue(toString) failed probably because of main thread stuff", e);
            mExamineeId.postValue(toString);
        }
    }

    public boolean isUniqueExamineeId(String examineeID) {
        synchronized (examineeIds){
            return !examineeIds.contains(cannonic(examineeID));
        }
    }

    private String cannonic(String examineeID) {
        return examineeID.replace('\\', '_').replace('/','_');
    }

    public boolean thereAreScannedCaptures() {
        return thereAreScannedCaptures;
    }

    public boolean isHoldingVersion() {
        return mVersion.getValue()!=null;
    }
}
