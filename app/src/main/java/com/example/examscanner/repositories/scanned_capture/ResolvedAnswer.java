package com.example.examscanner.repositories.scanned_capture;

import android.graphics.PointF;

import java.util.List;

public class ResolvedAnswer extends Answer{
    private final PointF upperLeft;
    private final PointF bottomRight;
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



}
