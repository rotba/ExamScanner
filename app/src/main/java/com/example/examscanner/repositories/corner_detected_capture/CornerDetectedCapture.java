package com.example.examscanner.repositories.corner_detected_capture;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;

public class CornerDetectedCapture {
    private int id;
    private Bitmap bm;
    private PointF upperLeft;
    private PointF upperRight;
    private PointF bottomRight;
    private PointF bottomLeft;

    public CornerDetectedCapture(int id, Bitmap bm, PointF upperLeft, PointF upperRight, PointF bottomRight, PointF bottomLeft) {
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
