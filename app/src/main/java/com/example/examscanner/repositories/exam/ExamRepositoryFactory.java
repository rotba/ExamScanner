package com.example.examscanner.repositories.exam;

import com.example.examscanner.repositories.Repository;

public class ExamRepositoryFactory {
    public Repository<Exam> create(){
        return ExamRepository.getInstance();
    }
}
