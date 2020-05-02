package com.example.examscanner.persistence.remote;

public class DatabaseFacadeFactory {
    public static DatabaseFacade instance;
    public static DatabaseFacade get(){
        if(instance==null){
            instance = new DatabaseFacadeImpl();
        }
        return instance;
    }
}
