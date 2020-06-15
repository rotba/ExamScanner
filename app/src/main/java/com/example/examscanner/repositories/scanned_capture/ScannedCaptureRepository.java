package com.example.examscanner.repositories.scanned_capture;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.examscanner.communication.CommunicationFacade;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.communication.entities_interfaces.ExamineeSolutionsEntityInterface;
import com.example.examscanner.communication.entities_interfaces.QuestionEntityInterface;
import com.example.examscanner.repositories.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class ScannedCaptureRepository implements Repository<ScannedCapture> {

    private static ScannedCaptureRepository instance;
    private int currAvailableId = 0;
    private ScannedCaptureConverter converter;
    private CommunicationFacade comFacade;
    private List<ScannedCapture> cache;


    public static ScannedCaptureRepository getInstance(){
        if (instance==null){
            final CommunicationFacade communicationFacade = new CommunicationFacadeFactory().create();
            instance = new ScannedCaptureRepository(
                    new ScannedCaptureConverter(communicationFacade),
                    communicationFacade
            );
            return instance;
        }else{
            return instance;
        }
    }

    static void tearDown() {
        instance=null;
    }

    public ScannedCaptureRepository(ScannedCaptureConverter converter, CommunicationFacade comFacade) {
        this.converter = converter;
        this.comFacade = comFacade;
        cache = new ArrayList<>();
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public ScannedCapture get(long id) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<ScannedCapture> get(Predicate<ScannedCapture> criteria) {
        List<ScannedCapture> ans = new ArrayList<>();
        for (ExamineeSolutionsEntityInterface ei: comFacade.getExamineeSoultions()) {
            ScannedCapture inCache = inCache(ei.getId());
            final ScannedCapture convert = inCache!=null? inCache:converter.convert(ei);
            if(criteria.test(convert)){
                ans.add(convert);
            }
        }
        return ans;
    }

    private ScannedCapture inCache(long id) {
        for (ScannedCapture scannedCapture : cache) {
            if(id== scannedCapture.getId()){
                return scannedCapture;
            }
        }
        return null;
    }

    @Override
    public void create(ScannedCapture scannedCapture) {
        final long id = comFacade.createExamineeSolution(
                -1,
                scannedCapture.getBm(),
                scannedCapture.getExamineeId(),
                scannedCapture.getVersion().getId()
        );

        float grade = 0;
        float questioneScore = (float)100 / (float)scannedCapture.getAnswers().size();
        long examID = scannedCapture.getVersion().getExam().getId();
        int versionNum = scannedCapture.getVersion().getNum();
        int[][] answersPrim = new int [scannedCapture.getAnswers().size()][1];
        for (Answer a:scannedCapture.getAnswers()) {
            answersPrim[a.getAnsNum()-1][0] = a.getSelection();
            QuestionEntityInterface origQuestion = comFacade.getQuestionByExamIdVerNumAndQNum(examID, versionNum, a.getAnsNum());
            if(a.getSelection() == origQuestion.getCorrectAnswer())
                grade+= questioneScore;
            comFacade.addExamineeAnswer(
                    id,
                    scannedCapture.getVersion().getQuestionByNumber(a.getAnsNum()).getId(),
                    a.getSelection(),
                    (int)(a.getLeft()*scannedCapture.getBm().getWidth()),
                    (int)(a.getUp()*scannedCapture.getBm().getHeight()),
                    (int)(a.getRight()*scannedCapture.getBm().getWidth()),
                    (int)(a.getBottom()*scannedCapture.getBm().getHeight())
            );
        }
        comFacade.addExamineeGrade(id, scannedCapture.getVersion().getId(), answersPrim , grade, scannedCapture.getOrigBitmap());
    }

    @Override
    public void update(ScannedCapture scannedCapture) {
        for (Answer a:scannedCapture.getAnswers()) {
            comFacade.updateExamineeAnswer(
                    scannedCapture.getId(),
                    scannedCapture.getVersion().getQuestionByNumber(a.getAnsNum()).getId(),
                    a.getSelection(),
                    (int)(a.getLeft()*scannedCapture.getBm().getWidth()),
                    (int)(a.getUp()*scannedCapture.getBm().getHeight()),
                    (int)(a.getRight()*scannedCapture.getBm().getWidth()),
                    (int)(a.getBottom()*scannedCapture.getBm().getHeight())
            );
        }
    }

    @Override
    public void delete(int id) {
        comFacade.deleteExamineeSolution(id);
    }

    @Override
    public void removeFromCache(long id) {
        comFacade.removeExamineeSolutionFromCache(id);
    }

//    @Override
//    public int genId() {
//        int ans = currAvailableId;
//        currAvailableId++;
//        return ans;
//    }
}
