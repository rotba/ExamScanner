package com.example.examscanner.repositories.session;

import com.example.examscanner.communication.CommunicationFacadeFactory;

public class ScanExamSessionProviderFactory {
    public ScanExamSessionsProvider create(){
        return new ScanExamSessionsProvider(new CommunicationFacadeFactory().create());
    }
}
