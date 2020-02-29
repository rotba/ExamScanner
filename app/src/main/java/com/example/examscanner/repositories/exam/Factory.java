package com.example.examscanner.repositories.exam;

import com.example.examscanner.repositories.Repository;

public class Factory {
    public Repository<Exam> create(){
        return ExamRepository.getInstance();
    }
}
