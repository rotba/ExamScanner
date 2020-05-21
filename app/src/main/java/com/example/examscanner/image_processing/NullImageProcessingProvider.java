package com.example.examscanner.image_processing;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;


public class NullImageProcessingProvider implements ImageProcessingFacade {


    @Override
    public Bitmap transformToRectangle(Bitmap bitmap, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft) {
        return null;
    }

    @Override
    public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer) {
        consumer.consume(0,null,null,null,null,null,null);
    }

    @Override
    public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer, float[] leftMostXs, float[] upperMostYs) {

    }

    @Override
    public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer) {
        consumer.consume(0,null,null,null,null,null,null);
    }

    @Override
    public Bitmap align(Bitmap bitmap, Bitmap perfectExamImg) {
        return null;
    }

    @Override
    public void detectCorners(Bitmap bm, DetectCornersConsumer consumer) {
        consumer.consume(new PointF(),null,null,new PointF());
    }
}
