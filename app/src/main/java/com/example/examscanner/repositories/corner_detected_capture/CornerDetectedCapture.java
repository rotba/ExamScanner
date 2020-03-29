package com.example.examscanner.repositories.corner_detected_capture;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.examscanner.repositories.version.Version;

public class CornerDetectedCapture {
    private long id;
    private Bitmap bm;
    private PointF upperLeft;
    private PointF upperRight;
    private PointF bottomRight;
    private PointF bottomLeft;
    private Version version;
    private long sessionId;

    public CornerDetectedCapture(Bitmap bm, PointF upperLeft, PointF upperRight, PointF bottomRight, PointF bottomLeft, Version version , long sessionId) {
        this.bm = bm;
        this.upperLeft = upperLeft;
        this.upperRight = upperRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;
        this.version = version;
        this.sessionId=sessionId;
    }

    public CornerDetectedCapture(Bitmap bitmap, int leftMostX, int upperMostY, int rightMostX, int bottomMosty, Version version, long sessionId) {
    }

    public void setId(long id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bm;
    }

    public long getId() {
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

    public int getLeftMostX() {
        return -1;
    }

    public int getUpperMostY() {
        return -1;
    }

    public int getRightMostX() {
        return -1;
    }

    public int getBottomMostY() {
        return -1;
    }

    public long getSession() {
        return sessionId;
    }

    public long getVersionId() {
        return version.getId();
    }
}
