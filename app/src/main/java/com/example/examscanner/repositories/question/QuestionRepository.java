package com.example.examscanner.repositories.question;

import com.example.examscanner.communication.CommunicationFacade;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.communication.entities_interfaces.QuestionEntityInterface;
import com.example.examscanner.repositories.Converter;
import com.example.examscanner.repositories.Repository;

import java.util.List;
import java.util.function.Predicate;

class QuestionRepository implements Repository<Question> {

    private final CommunicationFacade communicationFacade;
    private final Converter<QuestionEntityInterface, Question> converter;
    private static QuestionRepository instance;

    public static Repository<Question> getInstance() {
        if (instance==null){
            instance = new QuestionRepository(
                    new CommunicationFacadeFactory().create(),
                    new QuestionConverter()
            );
        }
        return instance;
    }

    private QuestionRepository(CommunicationFacade communicationFacade, Converter<QuestionEntityInterface, Question> converter) {
        this.communicationFacade = communicationFacade;
        this.converter = converter;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Question get(long id) {
        return null;
    }

    @Override
    public List<Question> get(Predicate<Question> criteria) {
        return null;
    }

    @Override
    public void create(Question question) {
        long id = communicationFacade.createQuestion(
                question.getVersionId(),
                question.getNum(),
                question.getAns(),
                question.getLeft(),
                question.getUp(),
                question.getRight(),
                question.getBottom()
        );
        question.setId(id);
    }

    @Override
    public void update(Question question) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public int genId() {
        return 0;
    }
}
