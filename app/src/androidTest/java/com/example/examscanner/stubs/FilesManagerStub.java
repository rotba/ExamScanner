package com.example.examscanner.stubs;

import android.graphics.Bitmap;

import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.persistence.local.files_management.FilesManager;

import java.io.FileNotFoundException;
import java.io.IOException;

public class FilesManagerStub implements FilesManager {
    @Override
    public Bitmap get(String id) throws FileNotFoundException {
        return BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_in1();
    }

    @Override
    public void store(Bitmap bm, String path) throws IOException {
    }

    @Override
    public void tearDown() {

    }

    @Override
    public String genId() {
        return null;
    }

    @Override
    public void delete(String bitmapPath) {

    }
}
