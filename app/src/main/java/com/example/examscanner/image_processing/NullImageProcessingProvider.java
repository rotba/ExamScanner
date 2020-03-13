package com.example.examscanner.image_processing;

import android.graphics.Bitmap;

public class NullImageProcessingProvider implements ImageProcessingFacade {
    @Override
    public ICornerDetectionResult detectCorners(Object o) {
        return new ICornerDetectionResult() {
            @Override
            public Bitmap getBitmap() {
                return null;
            }
        };
    }

    @Override
    public Object foo(Object d) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
