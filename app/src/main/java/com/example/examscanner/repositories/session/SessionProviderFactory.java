package com.example.examscanner.repositories.session;

import com.example.examscanner.communication.CommunicationFacadeFactory;

public class SessionProviderFactory {
    public SessionsProvider create(){
        return new SessionsProvider(new CommunicationFacadeFactory().create());
    }
}
