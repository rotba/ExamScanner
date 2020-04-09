package com.example.examscanner.repositories.version;

import com.example.examscanner.repositories.Repository;

public class VersionRepoFactory {
    private static boolean realInstanceNotReady = true;
    private static Repository<Version> stubInstance = null;

    public Repository<Version> create(){
        if(stubInstance!=null)
            return stubInstance;
        if (realInstanceNotReady)
            return new StubVersionRepository();
        return VersionRepository.getInstance();
    }

    public static void setStub(Repository theStubInstance) {
        stubInstance = theStubInstance;
    }
}
