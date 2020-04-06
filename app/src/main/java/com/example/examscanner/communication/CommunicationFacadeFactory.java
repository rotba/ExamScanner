package com.example.examscanner.communication;

import android.content.Context;

import com.example.examscanner.persistence.AppDatabase;

public class CommunicationFacadeFactory {


//    public static void tearDownTestMode() {
//    }

    public CommunicationFacade create() {
        return new FacadeImplProxy();

    }

//    public static void setUpTestMode(AppDatabase db) {
//        RealFacadeImple.setTestInstance(db);
//    }
}
