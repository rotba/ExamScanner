package com.example.examscanner.repositories.corner_detected_capture;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.examscanner.repositories.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CornerDetectedCaptureRepository implements Repository<CornerDetectedCapture> {
    private static CornerDetectedCaptureRepository instance;
    private List<CornerDetectedCapture> data = new ArrayList<>();
    public static CornerDetectedCaptureRepository getInstance(){
        if (instance==null){
            instance = new CornerDetectedCaptureRepository();
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
    public CornerDetectedCapture get(int id) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<CornerDetectedCapture> get(Predicate<CornerDetectedCapture> criteria) {
        List<CornerDetectedCapture> ans = new ArrayList<>();
        for (CornerDetectedCapture cdc: data) {
            if(criteria.test(cdc)) ans.add(cdc);
        }
        return ans;
    }

    @Override
    public void create(CornerDetectedCapture cornerDetectedCapture) {
        data.add(cornerDetectedCapture);
    }

    @Override
    public void update(CornerDetectedCapture cornerDetectedCapture) {

    }

    @Override
    public void delete(int id) {

    }
}
