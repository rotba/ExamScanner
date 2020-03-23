package com.example.examscanner.image_processing;

public class ImageProcessingFactory {
    private static ImageProcessingFacade testInstance;
    public ImageProcessingFacade create(){
        if (testInstance!=null) return testInstance;
        return new ImageProcessor();
    }
    public static void ONLYFORTESTINGsetTestInstance(ImageProcessingFacade thetestInstance){
        testInstance=thetestInstance;
    }
}
