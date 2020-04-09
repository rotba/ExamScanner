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
import com.example.examscanner.repositories.version.Version;

import java.util.ArrayList;
import java.util.List;

public class CreateExamModelView extends ViewModel {
    private MutableLiveData<Integer> addedVersions;
    private List<Version> versions;
    private ImageProcessingFacade imageProcessor;
    private ExamInCreation examCreated;
    private Repository<Exam> eRepo;
    private Repository<Version> vRepo;

    public CreateExamModelView(Repository<Exam> eRepo, Repository<Version> vRepo, ImageProcessingFacade imageProcessor, long sessionId) {
        this.eRepo = eRepo;
        this.vRepo = vRepo;
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


    public void addVersion(Bitmap bitmap) {
        imageProcessor.scanAnswers(bitmap, new ScanAnswersConsumer() {
            @Override
            public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                
            }
        });
    }
}
