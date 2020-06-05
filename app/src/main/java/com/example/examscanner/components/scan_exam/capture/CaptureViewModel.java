package com.example.examscanner.components.scan_exam.capture;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.Version;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;

import java.util.LinkedList;
import java.util.Queue;


public class CaptureViewModel extends ViewModel {
    private MutableLiveData<Integer> mNumOfTotalCaptures;
    private MutableLiveData<Integer> mNumOfProcessedCaptures;
    private Queue<Capture> unProcessedCaptures;
    private Repository<ScannedCapture> scRepo;
    private MutableLiveData<Version> mVersion;
    private MutableLiveData<String> mExamineeId;
    private ImageProcessingFacade imageProcessor;
    private long sessionId;
    private Exam exam;


    public CaptureViewModel(Repository<ScannedCapture> scRepo, ImageProcessingFacade imageProcessor, long sessionId, Exam exam) {
        this.exam = exam;
        unProcessedCaptures = new LinkedList<>();
        this.scRepo = scRepo;
        this.imageProcessor = imageProcessor;
//        mNumOfProcessedCaptures = new MutableLiveData<>(scRepo.get(sc -> sc.getSession() == sessionId).size());
        mNumOfProcessedCaptures = new MutableLiveData<>(scRepo.get(sc -> true).size());
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
        mNumOfTotalCaptures.setValue(mNumOfTotalCaptures.getValue() + 1);
    }

    public void processCapture() {
        Capture capture = unProcessedCaptures.remove();
        final Version version = getCurrentVersion().getValue();
        capture.setBitmap(imageProcessor.align(capture.getBitmap(), version.getPerfectImage()));
        imageProcessor.scanAnswers(
                capture.getBitmap(),
                exam.getNumOfQuestions(),
                new ScanAnswersConsumer() {
                    @Override
                    public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                        scRepo.create(new ScannedCapture(
                                scRepo.genId(), capture.getBitmap(), exam.getNumOfQuestions(), numOfAnswersDetected, answersIds, lefts, tops, rights, bottoms, selections, version, getCurrentExamineeId().getValue()

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
        mNumOfProcessedCaptures.postValue(scRepo.get(sc -> true).size());
    }

    public MutableLiveData<String> getCurrentExamineeId() {
        return mExamineeId;
    }

    public LiveData<Version> getCurrentVersion() {
        return mVersion;
    }

    public boolean isValidVersion() {
        return mVersion.getValue() != null;
    }

    public boolean isValidExamineeId() {
        return mExamineeId.getValue() != null;
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
