package com.example.examscanner.components.scan_exam.capture;

import android.graphics.Bitmap;

public class CornerDetectedCapture {
    private Bitmap bm;

    public CornerDetectedCapture(Bitmap bm) {
        this.bm = bm;
    }

    public Bitmap getBitmap() {
        return bm;
    }
}
