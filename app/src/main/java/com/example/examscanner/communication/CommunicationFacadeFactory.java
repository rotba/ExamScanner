package com.example.examscanner.communication;

public class CommunicationFacadeFactory {
    public static void tearDown() {
        RealFacadeImple.tearDown();
    }

    public static void setStatisticsProxy(StatisticsSourceStub sss) {
        FacadeImplProxy.setStatisticsProxy(sss);
    }


//    public static void tearDownTestMode() {
//    }

    public CommunicationFacade create() {
        return new FacadeImplProxy();

    }

//    public static void setUpTestMode(AppDatabase db) {
//        RealFacadeImple.setTestInstance(db);
//    }
}
