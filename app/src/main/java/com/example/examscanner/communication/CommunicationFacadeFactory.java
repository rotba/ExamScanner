package com.example.examscanner.communication;

public class CommunicationFacadeFactory {
    public Facade create(){
        return FacadeImpl.getInstance();
    }
}
