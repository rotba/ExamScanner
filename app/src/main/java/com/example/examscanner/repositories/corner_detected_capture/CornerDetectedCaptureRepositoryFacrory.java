package com.example.examscanner.repositories.corner_detected_capture;

import com.example.examscanner.repositories.Repository;

public class CornerDetectedCaptureRepositoryFacrory {
    private static Repository<CornerDetectedCapture> testInstance;
    public Repository<CornerDetectedCapture> create(){
        if (testInstance!=null)return testInstance;
        else return CornerDetectedCaptureRepository.getInstance();
    }

    public static void ONLYFORTESTINGsetTestInstance(Repository<CornerDetectedCapture> theTestInstance){
        testInstance =theTestInstance;
    }
}
