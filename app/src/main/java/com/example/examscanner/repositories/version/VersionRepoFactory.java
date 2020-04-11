package com.example.examscanner.repositories.version;

import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;

public class VersionRepoFactory {
    private static boolean realInstanceNotReady = false;
    private static Repository<Version> stubInstance = null;

    public Repository<Version> create(Exam e){
        if(stubInstance!=null)
            return stubInstance;
        if (realInstanceNotReady)
            return new StubVersionRepository(e);
        return VersionRepository.getInstance();
    }

    public static void setStub(Repository theStubInstance) {
        stubInstance = theStubInstance;
    }
}
