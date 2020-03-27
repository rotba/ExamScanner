package com.example.examscanner.image_processing;

import android.graphics.Bitmap;
import android.graphics.PointF;

import org.junit.Test;
import org.opencv.android.OpenCVLoader;


public class ImageProcessorTest {

    @Test
    public void detectCorners() {
        ImageProcessingFacade imageProcessor = new ImageProcessingFactory().create();
        Bitmap bm = null;
        OpenCVLoader.initDebug();
        imageProcessor.detectCorners(bm, new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {

            }
        });
    }
}