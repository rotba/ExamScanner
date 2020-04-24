package com.example.examscanner.persistence;

import androidx.room.Room;


import com.example.examscanner.communication.ContextProvider;

import static androidx.test.core.app.ApplicationProvider.*;


public class AppDatabaseFactory {
    private static AppDatabase instance;
    private static AppDatabase testInstance;
    private static boolean isTestMode = false;
    public static synchronized AppDatabase getInstance(){
        if(isTestMode){
            return getTestInstance();
        }else{
            return getRealInstance();
        }
    }
    private static AppDatabase getRealInstance(){
        if(instance==null){
            instance = Room.databaseBuilder(ContextProvider.get(),AppDatabase.class, "database-name").build();
        }
        return instance;
    }
    private static AppDatabase getTestInstance(){
        if(testInstance==null){
            testInstance = Room.inMemoryDatabaseBuilder(getApplicationContext(),AppDatabase.class).build();
        }
        return testInstance;
    }
    public static void setTestMode(){
        isTestMode=true;
    }

    public static void tearDownDb(){
        testInstance.clearAllTables();
    }
}