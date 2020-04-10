package com.example.examscanner.repositories.question;

import com.example.examscanner.repositories.Repository;

public class QuestionRepositoryFactory {
    public Repository<Question> create() {
        return QuestionRepository.getInstance();
    }
}
