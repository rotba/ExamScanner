package com.example.examscanner.communication;

public abstract class FacadeImpl implements CommunicationFacade {
    private static FacadeImplProxy instance;
    public static FacadeImpl getInstance(){
        if (instance==null){
            instance = new FacadeImplProxy();
            return instance;
        }else{
            return instance;
        }
    }
}
