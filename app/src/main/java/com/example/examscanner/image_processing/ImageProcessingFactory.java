package com.example.examscanner.image_processing;

import android.content.Context;

import com.example.examscanner.stubs.BitmapInstancesFactory;

public class ImageProcessingFactory {
    private static ImageProcessingFacade testInstance;
    private static ImageProcessingFacade stubInstance;
//    private BitmapInstancesFactory bmFact;

    public ImageProcessingFactory() {//TODO - remove dependency
    }

    public ImageProcessingFacade create(){
        if (testInstance!=null) return testInstance;
        if (stubInstance!=null) return stubInstance;
        return new ImageProcessor();
    }
    public static void ONLYFORTESTINGsetTestInstance(ImageProcessingFacade theStubInstance){
        stubInstance=theStubInstance;
    }
}
