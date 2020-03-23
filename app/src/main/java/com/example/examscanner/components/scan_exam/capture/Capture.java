package com.example.examscanner.components.scan_exam.capture;


import android.graphics.Bitmap;

import androidx.camera.core.ImageCapture;


public class Capture {
    private ImageCapture.OutputFileResults captureResults;
    private Bitmap bitmap;

    public Capture(ImageCapture.OutputFileResults captureResults) {
        this.captureResults = captureResults;
    }
    public Capture(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
}
