package com.example.examscanner.repositories.corner_detected_capture;

import android.graphics.Bitmap;
import android.graphics.Point;

public class CornerDetectedCapture {
    private Bitmap bm;
    private Point upperLeft;
    private Point upperRight;
    private Point bottomRight;
    private Point bottomLeft;

public CornerDetectedCapture(Bitmap bm, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft) {
        this.bm = bm;
        this.upperLeft = upperLeft;
        this.upperRight = upperRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;
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
