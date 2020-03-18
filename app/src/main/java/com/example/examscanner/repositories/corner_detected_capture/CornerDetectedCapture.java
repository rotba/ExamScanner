package com.example.examscanner.repositories.corner_detected_capture;

import android.graphics.Bitmap;
import android.graphics.Point;

public class CornerDetectedCapture {
    private Bitmap bm;

    public CornerDetectedCapture(Bitmap bm) {
        this.bm = bm;
    }

    public Bitmap getBitmap() {
        return bm;
    }
    public void setBitmap(Bitmap bm) {
        this.bm = bm;
    }

    public Point getUpperLeft() {
        return null;
    }

    public Point getUpperRight() {
        return null;
    }

    public Point getBottomRight() {
        return null;
    }

    public Point getBottomLeft() {
        return null;
    }
}
