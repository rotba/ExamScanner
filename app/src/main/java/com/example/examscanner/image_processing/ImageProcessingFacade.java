package com.example.examscanner.image_processing;

import android.graphics.Bitmap;
import android.graphics.Point;

public interface ImageProcessingFacade {
    public Object foo(Object d);
    public ICornerDetectionResult detectCorners(Object o);
    public Bitmap transformToRectangle(Bitmap bitmap, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft);
    IScannedCapture scanAnswers(Bitmap bitmap);
}
