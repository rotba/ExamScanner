package com.example.examscanner.components.scan_exam.detect_corners;

import android.view.View;

interface ProgressBarHandler {
    public void onProcessingBegun();
    public void onProcessingFinished();
    public void setContextView(View view);
    public boolean isInProgress();
}
