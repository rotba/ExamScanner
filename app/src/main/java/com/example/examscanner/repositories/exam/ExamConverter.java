package com.example.examscanner.repositories.exam;

import com.example.examscanner.communication.CommunicationFacade;
import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.communication.entities_interfaces.QuestionEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;
import com.example.examscanner.repositories.Converter;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.question.Question;
import com.example.examscanner.repositories.version.Version;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExamConverter implements Converter<ExamEntityInterface, Exam> {
    private CommunicationFacade communicationFacade;

    public ExamConverter(CommunicationFacade communicationFacade) {
        this.communicationFacade = communicationFacade;
    }

    @Override
    public Exam convert(final ExamEntityInterface examEntityInterface) {
        Exam e = null;
        VersionsFuture vf = new VersionsFuture(e,examEntityInterface.getVersionsIds());
        e = new Exam(
                null,
                examEntityInterface.getID(),
                vf,
                new ArrayList<>(),
                examEntityInterface.getCourseName(),
                examEntityInterface.getTerm(),
                examEntityInterface.getSemester(),
                examEntityInterface.getSessionId(),
                examEntityInterface.getYear()
        );
        return e;
    }

    private class VersionsFuture implements Future<List<Version>>{
        private final Exam exam;
        private final long[] vIds;

        public VersionsFuture(Exam exam, long[] vIds) {
            this.exam = exam;
            this.vIds = vIds;
        }

        private List<Version> lazy = null;
        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {return false;}

        @Override
        public boolean isCancelled() {return false;}

        @Override
        public boolean isDone() {
            return lazy==null;
        }

        @Override
        public List<Version> get() throws ExecutionException, InterruptedException {
            if(!isDone()){
                lazy = create();
            }
            return lazy;
        }

        private List<Version> create() {
            lazy = new ArrayList<>();
            for (long vId:vIds) {
                VersionEntityInterface vei = communicationFacade.getVersionById(vId);
                Version v =  new Version(vei.getNumber(), exam);
                QuestionsFuture qf = new QuestionsFuture(v, vei.getQuestions());
                v.setQuestionsFuture(qf);
                lazy.add(v);
            }
            return lazy;
        }

        @Override
        public List<Version> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {return null;}
    }

    private class QuestionsFuture implements Future<List<Question>> {
        private final Version v;
        private final long[] questions;
        private List<Question> lazy;

        public QuestionsFuture(Version v, long[] questions) {
            this.v = v;
            this.questions = questions;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {return false;}

        @Override
        public boolean isCancelled() {return false;}

        @Override
        public boolean isDone() {return lazy==null;}

        @Override
        public List<Question> get() throws ExecutionException, InterruptedException {
            if(!isDone()){
                lazy = create();
            }
            return lazy;
        }

        private List<Question> create() {
            lazy = new ArrayList<>();
            for (long qId: communicationFacade.getQuestionsByVersionId(v.getId())) {
                QuestionEntityInterface qei = communicationFacade.getQuestionById(qId);
                lazy.add(new Question(qId,v,qei.getNum(),(int)qei.getCorrectAnswer(),qei.getLeftX(),qei.getUpY(),qei.getRightX(),qei.getBottomY()));
            }
            return lazy;
        }

        @Override
        public List<Question> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {return null;}
    }
}
