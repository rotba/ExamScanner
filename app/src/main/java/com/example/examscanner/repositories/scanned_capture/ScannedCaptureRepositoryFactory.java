package com.example.examscanner.repositories.scanned_capture;

import com.example.examscanner.repositories.Repository;

public class ScannedCaptureRepositoryFactory {
    public Repository<ScannedCapture> create(){
        return ScannedCaptureRepository.getInstance();
    }
}
