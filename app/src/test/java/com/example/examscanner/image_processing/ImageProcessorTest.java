package com.example.examscanner.image_processing;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.example.examscanner.stubs.BitmapInatancesFactory;

import org.junit.Test;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;


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