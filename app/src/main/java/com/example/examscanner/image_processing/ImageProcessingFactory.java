package com.example.examscanner.image_processing;

import com.example.examscanner.stubs.BitmapInstancesFactory;

public class ImageProcessingFactory {
    private static ImageProcessingFacade testInstance;
    private static ImageProcessingFacade stubInstance;
    private BitmapInstancesFactory bmFact;

    public ImageProcessingFactory(BitmapInstancesFactory bmFact) {//TODO - remove dependency
        this.bmFact = bmFact;
    }

    public ImageProcessingFacade create(){
        if (testInstance!=null) return testInstance;
        if (stubInstance!=null) return stubInstance;
        return new ImageProcessor(bmFact);
    }
    public static void ONLYFORTESTINGsetTestInstance(ImageProcessingFacade theStubInstance){
        stubInstance=theStubInstance;
    }
}
