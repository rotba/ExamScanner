package com.example.examscanner.communication;

public class CommunicationFacadeFactory {
    private static boolean testMode = false;

    public Facade create() {
        if(testMode){
            return FacadeImpl.getInstance();
        }else{
            return FacadeImpl.getInstance();
        }

    }

    public static void setTestMode() {
        testMode = true;
    }
}
