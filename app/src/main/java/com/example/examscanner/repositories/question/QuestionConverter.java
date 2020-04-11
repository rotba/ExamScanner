package com.example.examscanner.repositories.question;

import com.example.examscanner.communication.entities_interfaces.QuestionEntityInterface;
import com.example.examscanner.repositories.Converter;

class QuestionConverter implements Converter<QuestionEntityInterface, Question> {
    @Override
    public Question convert(QuestionEntityInterface ei) {
        return new Question(
                ei.getId(),
                null,
                ei.getNum(),
                (int) ei.getCorrectAnswer(),
                ei.getLeftX(),
                ei.getUpY(),
                ei.getRightX(),
                ei.getBottomY()
        );
    }
}
