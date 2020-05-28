package com.example.examscanner.use_case_contexts_creators;

import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;

public class CornerDetectionContext3Setuper extends CornerDetectionContext2Setuper {
    private static final String DONT_KNOW_EAMINEE_ID = "DONT_KNOW_EAMINEE_ID";
    private ScannedCapture sc;

    @Override
    public void setup() {
        super.setup();
        ScannedCaptureRepositoryFactory.tearDown();
        scRepo = new ScannedCaptureRepositoryFactory().create();
        getCDC().setBitmap(
                imageProcessor.align(
                        getCDC().getBitmap(),
                        getVersion().getPerfectImage()
                )
        );
        imageProcessor.scanAnswers(
                getCDC().getBitmap(),
                getTheExam().getNumOfQuestions(),
                new ScanAnswersConsumer() {
                    @Override
                    public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                        sc = new ScannedCapture(-1, getCDC().getBitmap(), getTheExam().getNumOfQuestions(), numOfAnswersDetected, answersIds, lefts, tops, rights, bottoms, selections, getCDC().getVersion(), DONT_KNOW_EAMINEE_ID);
                        getSCRepo().create(sc);
                    }
                },
                getVersion().getRealtiveLefts(),
                getVersion().getRealtiveUps()
        );
    }

    public ScannedCapture getSc() {
        return sc;
    }
}
