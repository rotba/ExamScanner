package com.example.examscanner.image_processing;

public class NullImageProcessingProvider implements ImageProcessingFacade {
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
