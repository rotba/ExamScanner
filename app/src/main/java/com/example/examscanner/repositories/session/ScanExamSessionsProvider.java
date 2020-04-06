package com.example.examscanner.repositories.session;

import com.example.examscanner.communication.CommunicationFacade;

public class ScanExamSessionsProvider {
    CommunicationFacade comFacade;
    ScanExamSessionsProvider(CommunicationFacade comFacade){
        this.comFacade = comFacade;
    }
    public long provide(long exanId){
        return comFacade.createNewScanExamSession(exanId);
    }
}
