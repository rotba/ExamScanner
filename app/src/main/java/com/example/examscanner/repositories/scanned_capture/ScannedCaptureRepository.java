package com.example.examscanner.repositories.scanned_capture;

import com.example.examscanner.repositories.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ScannedCaptureRepository implements Repository<ScannedCapture> {

    private List<ScannedCapture> data  = new ArrayList<>();
    private static ScannedCaptureRepository instance;
    private int currAvailableId = 0;

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
    public ScannedCapture get(long id) {
        return null;
    }

    @Override
    public List<ScannedCapture> get(Predicate<ScannedCapture> criteria) {
        return data;
    }

    @Override
    public void create(ScannedCapture scannedCapture) {
        data.add(scannedCapture);
    }

    @Override
    public void update(ScannedCapture scannedCapture) {

    }

    @Override
    public void delete(int id) {

    }
    @Override
    public int genId() {
        int ans = currAvailableId;
        currAvailableId++;
        return ans;
    }
}
