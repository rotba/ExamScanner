package com.example.examscanner.persistence.entities;

import android.graphics.Bitmap;
import android.graphics.PointF;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class SemiScannedCapture {
    public static final String pkName = "id";
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int leftMostX;
    private int upperMostY;
    private int rightMostX;
    private int bottomMostY;
    private int bitmapId;
    public static final String fkVersionId = "versionId";
    @ForeignKey(entity = Version.class, parentColumns = {Version.pkName}, childColumns = {"versionId"})
    private long versionId;
    public static final String fkSessionId = "sessionId";
    @ForeignKey(entity = Session.class, parentColumns = {Session.pkName}, childColumns = {"sessionId"})
    private long sessionId;

    public SemiScannedCapture(int leftMostX, int upperMostY, int rightMostX, int bottomMostY, int bitmapId, long versionId, long sessionId) {
        this.leftMostX = leftMostX;
        this.upperMostY = upperMostY;
        this.rightMostX = rightMostX;
        this.bottomMostY = bottomMostY;
        this.bitmapId = bitmapId;
        this.versionId = versionId;
        this.sessionId = sessionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeftMostX() {
        return leftMostX;
    }

    public void setLeftMostX(int leftMostX) {
        this.leftMostX = leftMostX;
    }

    public int getUpperMostY() {
        return upperMostY;
    }

    public void setUpperMostY(int upperMostY) {
        this.upperMostY = upperMostY;
    }

    public int getRightMostX() {
        return rightMostX;
    }

    public void setRightMostX(int rightMostX) {
        this.rightMostX = rightMostX;
    }

    public int getBottomMostY() {
        return bottomMostY;
    }

    public void setBottomMostY(int bottomMostY) {
        this.bottomMostY = bottomMostY;
    }

    public int getBitmapId() {
        return bitmapId;
    }

    public void setBitmapId(int bitmapId) {
        this.bitmapId = bitmapId;
    }

    public long getVersionId() {
        return versionId;
    }

    public void setVersionId(long versionId) {
        this.versionId = versionId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }
}
