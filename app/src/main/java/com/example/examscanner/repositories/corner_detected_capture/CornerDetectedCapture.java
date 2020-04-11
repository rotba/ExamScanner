package com.example.examscanner.repositories.corner_detected_capture;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.examscanner.repositories.version.Version;

public class CornerDetectedCapture {
    private long id;
    private Bitmap bm;
    private long sessionId;
    private int leftMostX;
    private int upperMostY;
    private int rightMostX;
    private int bottomMosty;
    private Long versionId;


    public CornerDetectedCapture(Bitmap bitmap, int leftMostX, int upperMostY, int rightMostX, int bottomMosty,long sessionId) {
        this.leftMostX = leftMostX;
        this.upperMostY = upperMostY;
        this.rightMostX = rightMostX;
        this.bottomMosty = bottomMosty;
        this.bm = bitmap;
        this.sessionId=sessionId;
    }

    public CornerDetectedCapture(Bitmap bitmap, PointF upperLeft, PointF bottomRight, long sessionId) {
        this.leftMostX = (int)upperLeft.x;
        this.upperMostY = (int)upperLeft.y;
        this.rightMostX = (int)bottomRight.x;
        this.bottomMosty = (int)bottomRight.y;
        this.bm = bitmap;
        this.sessionId=sessionId;
    }

    public CornerDetectedCapture() {

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
        return leftMostX;
    }

    public int getUpperMostY() {
        return upperMostY;
    }

    public int getRightMostX() {
        return rightMostX;
    }

    public int getBottomMostY() {
        return bottomMosty;
    }

    public long getSession() {
        return sessionId;
    }

    public void setVersionId(long id) {
        versionId = id;
    }

    public boolean hasVersion() {
        return versionId!=null;
    }
}
