package com.example.examscanner.image_processing;

public class ImageProcessingFactory {
    public ImageProcessingFacade create(){
        return new NullImageProcessingProvider();
    }
}
