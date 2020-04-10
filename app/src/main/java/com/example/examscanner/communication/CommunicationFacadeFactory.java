package com.example.examscanner.communication;

import android.content.Context;

public class CommunicationFacadeFactory {
    private static boolean testMode = false;

    public CommunicationFacade create() {
        if(testMode){
            return new FacadeImplProxy();
        }else{
            return new FacadeImplProxy();
        }

    }

    public static void setTestMode() {
        testMode = true;
    }
}
