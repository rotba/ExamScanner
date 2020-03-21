package com.example.examscanner.image_processing;

public class ImageProcessingFactory {
    private static ImageProcessingFacade testInstance;
    public ImageProcessingFacade create(){
        if (testInstance!=null) return testInstance;
        return new NullImageProcessingProvider();
    }
    public static void ONLYFORTESTINGsetTestInstance(ImageProcessingFacade thetestInstance){
        testInstance=thetestInstance;
    }
}
