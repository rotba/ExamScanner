package com.example.examscanner.repositories.grader;

import com.example.examscanner.repositories.Repository;

public class GraderRepoFactory {
    public Repository<Grader> create(){
        return GraderRepository.getInstance();
    }
}
