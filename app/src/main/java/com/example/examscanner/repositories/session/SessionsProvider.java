package com.example.examscanner.repositories.session;

import com.example.examscanner.communication.CommunicationFacade;

public class SessionsProvider {
    CommunicationFacade comFacade;
    SessionsProvider(CommunicationFacade comFacade){
        this.comFacade = comFacade;
    }
    public long provide(String description){
        return comFacade.createNewSession(description);
    }
}
