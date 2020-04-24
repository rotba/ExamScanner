package com.example.examscanner.state;

public class StateFactory {
    private static State instance;
    public static State get(){
        if (instance == null){
            instance = new AnonymousState();
        }
        return instance;
    }
}
