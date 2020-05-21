package com.example.examscanner.use_case_contexts_creators;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.components.create_exam.CreateExamModelView;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.detect_corners.DCEmptyRepositoryFactory;
import com.example.examscanner.components.scan_exam.reslove_answers.SCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.image_processing.ImageProcessor;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.grader.GraderRepoFactory;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.session.SESessionProviderFactory;

import org.opencv.android.OpenCVLoader;

import java.util.List;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;

public class CornerDetectionContext2Setuper {
    protected Repository<Exam> examRepository;
    protected ImageProcessingFacade imageProcessor;
    protected Repository<CornerDetectedCapture> cdcRepo;
    protected Repository<ScannedCapture> scRepo;
    protected long scanExamSession;
    protected Exam e;
    protected int dinaBarzilayVersion;
    protected CreateExamModelView ceModelView;
    protected final CornerDetectedCapture[] cdCaptures = new CornerDetectedCapture[1];
    protected Bitmap createExamBitmap;
    private Bitmap theCDCBitmap;

    public CornerDetectionContext2Setuper(Bitmap cdcBitmap) {
        this.theCDCBitmap=cdcBitmap;
    }
    public CornerDetectionContext2Setuper(Bitmap cdcBitmap, Bitmap examBitmap) {
        this.theCDCBitmap=cdcBitmap;
        createExamBitmap =examBitmap;
    }
    public CornerDetectionContext2Setuper() {}

    public void setup(){
        OpenCVLoader.initDebug();
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(new ImageProcessor(getApplicationContext()));
        imageProcessor = new ImageProcessingFactory().create();
        ceModelView  = new CreateExamModelView(
                new ExamRepositoryFactory().create(),
                new GraderRepoFactory().create(),
                imageProcessor,
                StateFactory.get(),
                0
        );

        dinaBarzilayVersion = 496351;
        ceModelView.holdNumOfQuestions("50");
        int numOfQuestions = ceModelView.getExam().getNumOfQuestions();
        if(createExamBitmap==null){
            createExamBitmap =BitmapsInstancesFactoryAndroidTest.getComp191_v1_JPG_ANS_2();
        }
        ceModelView.holdVersionBitmap(createExamBitmap);
        ceModelView.holdVersionNumber(dinaBarzilayVersion);
        ceModelView.addVersion();
        ceModelView.create("testAddVersion()_courseName","A","Fall","2020");
        examRepository = new ExamRepositoryFactory().create();
        List<Exam> exams = examRepository.get((e)->true);
        e = exams.get(0);


        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
        scRepo = SCEmptyRepositoryFactory.create();
     //   ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(scRepo);
        cdcRepo = new CDCRepositoryFacrory().create();
        scanExamSession = new SESessionProviderFactory().create().provide(e.getId());
        if(theCDCBitmap==null){
            theCDCBitmap = BitmapsInstancesFactoryAndroidTest.getComp191_V1_PDF_Auth_No_Flash();
        }
        imageProcessor.detectCorners(theCDCBitmap, new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                cdCaptures[0] = new CornerDetectedCapture(theCDCBitmap, upperLeft, upperRight,bottomRight,bottomLeft, scanExamSession);
                cdcRepo.create(cdCaptures[0]);
            }
        });
        e.setNumOfQuestions(50);
        getCDC().setVersion(e.getVersionByNum(dinaBarzilayVersion));
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

    public void tearDown() {
//        ((ExamRepositoryStub)examRepository).tearDown();
    }

    public CornerDetectedCapture getCDC() {
        return cdCaptures[0];
    }

    public int getVersionNum() {
        return dinaBarzilayVersion;
    }

    public Bitmap getOrigVersionImage() {
        return createExamBitmap;
    }
}
