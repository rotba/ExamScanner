package com.example.examscanner.components.create_exam;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamInCreation;
import com.example.examscanner.repositories.question.Question;
import com.example.examscanner.repositories.scanned_capture.Answer;
import com.example.examscanner.repositories.scanned_capture.ResolvedAnswer;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.version.Version;

import java.util.ArrayList;
import java.util.List;

public class CreateExamModelView extends ViewModel {
    private MutableLiveData<Integer> addedVersions;
    private List<Version> versions;
    private Repository<Question> qRepo;
    private ImageProcessingFacade imageProcessor;
    private ExamInCreation examCreated;
    private Repository<Exam> eRepo;
    private Repository<Version> vRepo;
    private Bitmap currentVersionBitmap;
    private Integer currentVersionNumber;


    public CreateExamModelView(Repository<Exam> eRepo, Repository<Version> vRepo, Repository<Question>  qRepo, ImageProcessingFacade imageProcessor, long sessionId) {
        this.eRepo = eRepo;
        this.vRepo = vRepo;
        this.qRepo = qRepo;
        this.imageProcessor = imageProcessor;
        examCreated = new ExamInCreation(sessionId);
        versions = new ArrayList<>();
        eRepo.create(examCreated);
        addedVersions = new MutableLiveData<>(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void create(String courseName, String term, String semester, String year){
        eRepo.update(
                examCreated.commit(
                        courseName,
                        Term.createByViewValue(term).getValue(),
                        Semester.createByViewValue(semester).getValue(),
                        versions.stream().mapToLong(Version::getId).toArray(),
                        year
                )
        );
    }

    public MutableLiveData<Integer> getAddedVersions() {
        return addedVersions;
    }



    public void holdVersionBitmap(Bitmap bitmap){
        currentVersionBitmap = bitmap;

    }
    public void addVersion() {
        imageProcessor.scanAnswers(currentVersionBitmap, new ScanAnswersConsumer() {
            @Override
            public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                ScannedCapture scannedCapture = new ScannedCapture(-1,null,numOfAnswersDetected,numOfAnswersDetected,answersIds,lefts,tops,rights,bottoms,selections);
                if(versionScanningWentWell(scannedCapture))
                    throw new VersionScanningDidntGoWell();
                Version v = new Version(currentVersionNumber,examCreated.getId(),new long[0]);
                vRepo.create(v);
                for (ResolvedAnswer ans: scannedCapture.getResolvedAnswers()) {
                    qRepo.create(new Question(
                            v.getId(),
                            ans.getAnsNum(),
                            ans.getSelection(),
                            (int)ans.getUpperLeft().x*currentVersionBitmap.getWidth(),
                            (int)ans.getUpperLeft().y*currentVersionBitmap.getHeight(),
                            (int)ans.getBottomRight().x*currentVersionBitmap.getWidth(),
                            (int)ans.getBottomRight().y*currentVersionBitmap.getHeight()
                    ));
                }
            }
        });
    }

    private boolean versionScanningWentWell(ScannedCapture scannedCapture) {
        return false;
    }

    public Exam getExam() {
        return examCreated;
    }

    public void holdVersionNumber(Integer verNum) {
        this.currentVersionNumber = verNum;
    }

    public void incNumOfVersions() {
        addedVersions.setValue(addedVersions.getValue()+1);
    }

    public Bitmap getCurrentVersionBitmap() {
        return currentVersionBitmap;
    }

    public Integer getCurrentVersionNumber() {
        return currentVersionNumber;
    }
}
