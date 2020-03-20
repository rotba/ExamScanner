package com.example.examscanner.components.scan_exam.capture;


import android.graphics.Bitmap;

import androidx.camera.core.ImageCapture;


public class Capture {
    private ImageCapture.OutputFileResults captureResults;

    public Capture(ImageCapture.OutputFileResults captureResults) {
        this.captureResults = captureResults;
    }
    public Bitmap getBitmap(){
        return null;
    }
}
