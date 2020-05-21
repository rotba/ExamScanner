package com.example.examscanner.persistence.local.files_management;

import com.example.examscanner.communication.ContextProvider;

import org.jetbrains.annotations.NotNull;

public class FilesManagerFactory {
    public static boolean testMode = false;
    private static FilesManager instance;
    private static FilesManager testInstance;
    public static FilesManager create(){
        if(testMode) {
            return getTestsInstance();
        }else{
            return getRealInstance();
        }
    }

    private static FilesManager getRealInstance() {
        if(instance == null){
            instance = new FilesManagerImpl(ContextProvider.get());
        }
        return instance;
    }

    @NotNull
    protected static FilesManager getTestsInstance() {
        if(testInstance == null){
            testInstance = new StubFilesManager();
        }
        return testInstance;
    }

    public static void tearDown() {
        if(testInstance!=null)testInstance.tearDown();
        if(instance!=null)instance.tearDown();
    }
}
