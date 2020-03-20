package com.example.examscanner.repositories.scanned_capture;

import android.graphics.PointF;

import java.util.List;

public class ConflictedAnswer extends Answer{
    private final PointF upperLeft;
    private final PointF bottomRight;

    public ConflictedAnswer(int id, PointF upperLeft, PointF bottomRight) {
        super(id);
        this.upperLeft = upperLeft;
        this.bottomRight = bottomRight;
    }

    @Override
    public boolean isConflicted() {
        return true;
    }

    @Override
    public void addMe(List<ConflictedAnswer> l) {
        l.add(this);
    }

    public PointF getUpperLeft() {
        return upperLeft;
    }

    public PointF getBottomRight() {
        return bottomRight;
    }
}
