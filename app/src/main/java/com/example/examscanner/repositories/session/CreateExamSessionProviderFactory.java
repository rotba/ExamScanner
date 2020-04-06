package com.example.examscanner.repositories.session;

import com.example.examscanner.communication.CommunicationFacadeFactory;

public class CreateExamSessionProviderFactory {
    public CreateExamSessionsProvider create(){
        return new CreateExamSessionsProvider(new CommunicationFacadeFactory().create());
    }
}
