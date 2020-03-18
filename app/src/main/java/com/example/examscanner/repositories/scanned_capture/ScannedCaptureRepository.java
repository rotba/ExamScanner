package com.example.examscanner.repositories.scanned_capture;

import com.example.examscanner.repositories.Repository;

import java.util.List;
import java.util.function.Predicate;

public class ScannedCaptureRepository implements Repository<ScannedCapture> {

    private static ScannedCaptureRepository instance;
    public static ScannedCaptureRepository getInstance(){
        if (instance==null){
            instance = new ScannedCaptureRepository();
            return instance;
        }else{
            return instance;
        }
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public ScannedCapture get(int id) {
        return null;
    }

    @Override
    public List<ScannedCapture> get(Predicate<ScannedCapture> criteria) {
        return null;
    }

    @Override
    public void create(ScannedCapture scannedCapture) {

    }

    @Override
    public void update(ScannedCapture scannedCapture) {

    }

    @Override
    public void delete(int id) {

    }
}
