package com.example.examscanner.repositories.session;

import com.example.examscanner.communication.CommunicationFacade;

public class CreateExamSessionsProvider {
    private CommunicationFacade communicationFacade;

    public CreateExamSessionsProvider(CommunicationFacade communicationFacade) {
        this.communicationFacade = communicationFacade;
    }
    public long provide(){
        return communicationFacade.createNewCreateExamSession();
    }
}
