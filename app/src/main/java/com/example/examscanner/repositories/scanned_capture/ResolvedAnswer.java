package com.example.examscanner.repositories.scanned_capture;

import android.graphics.PointF;

import androidx.annotation.NonNull;

import java.util.List;

public class ResolvedAnswer extends Answer{
    private final PointF upperLeft;
    private final PointF bottomRight;

    public PointF getUpperLeft() {
        return upperLeft;
    }

    public PointF getBottomRight() {
        return bottomRight;
    }

    private final int selection;

    public ResolvedAnswer(int id, PointF upperLeft, PointF bottomRight, int selection) {
        super(id);
        this.upperLeft = upperLeft;
        this.bottomRight = bottomRight;
        this.selection = selection;
    }

    @Override
    public boolean isResolved() {
        return true;
    }


    public int getSelection() {
        return selection;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(selection);
    }
}
