package com.example.examscanner.use_case_contexts_creators;

import android.graphics.PointF;

import com.example.examscanner.authentication.CalimentAuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.State;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.authentication.state.StateHolder;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.detect_corners.DCEmptyRepositoryFactory;
import com.example.examscanner.components.scan_exam.reslove_answers.SCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.exam.Version;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.repositories.session.ScanExamSessionProviderFactory;
import com.example.examscanner.stubs.ExamStubFactory;

import java.util.ArrayList;

import io.reactivex.observers.TestObserver;

import static com.example.examscanner.ImageProcessorsGenerator.fakeIP;
import static com.example.examscanner.ImageProcessorsGenerator.nullIP;

public class CornerDetectionContext1Setuper {
    private Repository<Exam> examRepository;
    private ImageProcessingFacade imageProcessor;
    private Repository<CornerDetectedCapture> cdcRepo;
    Repository<ScannedCapture> scRepo;
    private long scanExamSession;
    private Exam e;
    private int dinaBarzilayVersion;
    private int theDevilVersion;
//    private String uId;

    public void setup(){
//        final TestObserver observer = new TestObserver<String>() {
//            @Override
//            public void onNext(String id) {
//                super.onNext(id);
//                uId = id;
//            }
//        };
//        CalimentAuthenticationHandlerFactory.getTest().generateAuthenticationAndReturnId().subscribe(observer);
//        observer.awaitCount(1);
        e = new Exam(null,-1,Exam.theEmptyFutureVersionsList(),new ArrayList<>(),"theTestExamCourseName",0,0,0,"2020");
        examRepository = new ExamRepositoryFactory().create();
        dinaBarzilayVersion = 496351;
        e.addVersion(new Version(-1, dinaBarzilayVersion,e,Version.theEmptyFutureQuestionsList()));
        theDevilVersion = 666;
        e.addVersion(new Version(-1, theDevilVersion,e,Version.theEmptyFutureQuestionsList()));
        examRepository.create(e);
        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
        scRepo = SCEmptyRepositoryFactory.create();
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(scRepo);
        imageProcessor = fakeIP();
        cdcRepo = new CDCRepositoryFacrory().create();
        scanExamSession = new ScanExamSessionProviderFactory().create().provide(e.getId());
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), upperLeft, upperRight,bottomRight,bottomLeft, scanExamSession));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg2(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), upperLeft, upperRight,bottomRight,bottomLeft, scanExamSession));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg3(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), upperLeft, upperRight,bottomRight,bottomLeft, scanExamSession));
            }
        });
//        StateFactory.get().logout(StateHolder.getDefaultHolder());
    }

    public ImageProcessingFacade getImageProcessor() {
        return imageProcessor;
    }

    public Repository<CornerDetectedCapture> getCDCRepo() {
        return cdcRepo;
    }

    public Repository<ScannedCapture> getSCRepo() {
        return scRepo;
    }

    public Exam getTheExam() {
        return e;
    }

    public int getDinaBarzilayVersion() {
        return dinaBarzilayVersion;
    }

    public int getTheDevilVersion() {
        return theDevilVersion;
    }
}
