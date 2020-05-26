package com.example.examscanner.repositories.scanned_capture;

import com.example.examscanner.communication.CommunicationFacade;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.communication.entities_interfaces.ExamineeSolutionsEntityInterface;
import com.example.examscanner.repositories.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ScannedCaptureRepository implements Repository<ScannedCapture> {

    private static ScannedCaptureRepository instance;
    private int currAvailableId = 0;
    private ScannedCaptureConverter converter;
    private CommunicationFacade comFacade;

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
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public ScannedCapture get(long id) {
        return null;
    }

    @Override
    public List<ScannedCapture> get(Predicate<ScannedCapture> criteria) {
        List<ScannedCapture> ans = new ArrayList<>();
        for (ExamineeSolutionsEntityInterface ei: comFacade.getExa) {

        }
    }

    @Override
    public void create(ScannedCapture scannedCapture) {
        final long id = comFacade.createExamineeSolution(
                -1,
                scannedCapture.getBm(),
                scannedCapture.getExamineeId()
        );
        for (Answer a:scannedCapture.getAnswers()) {
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
    }

    @Override
    public void update(ScannedCapture scannedCapture) {

    }

    @Override
    public void delete(int id) {

    }
    @Override
    public int genId() {
        int ans = currAvailableId;
        currAvailableId++;
        return ans;
    }
}
