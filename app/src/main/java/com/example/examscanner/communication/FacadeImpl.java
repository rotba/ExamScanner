package com.example.examscanner.communication;

import org.json.JSONObject;

public abstract class FacadeImpl implements Facade {
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
