package com.example.examscanner.persistence.local.files_management;

import android.content.Context;
import android.util.Log;

import com.example.examscanner.communication.ContextProvider;
import com.example.examscanner.communication.RealFacadeImple;
import com.example.examscanner.stubs.FilesManagerStub;

import org.jetbrains.annotations.NotNull;

public class FilesManagerFactory {
    private static boolean testMode = false;
    private static boolean QAD_testMode = true;
    private static FilesManager instance;
    public static FilesManager create(){
        if(QAD_testMode){
            Log.d("ExamScanner", "DONT GORGET ME!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return getStubInstance();
        }
        return getRealInstance();
    }

    private static FilesManager getStubInstance() {
        if(instance == null){
            instance = new FilesManagerStub();
        }
        return instance;
    }

    private static FilesManager getRealInstance() {
        if(instance == null){
            instance = new FilesManagerImpl(ContextProvider.get());
        }
        return instance;
    }



    public static void tearDown() {
        if(instance!=null)instance.tearDown();
        instance= null;
    }

    public static void setTestMode(Context c) {
        FilesManagerImpl.setTestMode();
        instance = new FilesManagerImpl(c);
    }

    public static void setStubInstance(FilesManager fm){
        if(QAD_testMode){
            instance = getStubInstance();
        }else{
            instance=fm;
        }
    }
}
