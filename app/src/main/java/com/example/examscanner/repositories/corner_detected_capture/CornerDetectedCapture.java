package com.example.examscanner.repositories.corner_detected_capture;

import android.graphics.Bitmap;
import android.graphics.Point;

public class CornerDetectedCapture {
    private int id;
    private Bitmap bm;
    private Point upperLeft;
    private Point upperRight;
    private Point bottomRight;
    private Point bottomLeft;

    public CornerDetectedCapture(int id, Bitmap bm, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft) {
        this.id = id;
        this.bm = bm;
        this.upperLeft = upperLeft;
        this.upperRight = upperRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;
    }

    public Bitmap getBitmap() {
        return bm;
    }

    public int getId() {
        return id;
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
