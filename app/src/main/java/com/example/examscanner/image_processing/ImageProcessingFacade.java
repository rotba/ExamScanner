package com.example.examscanner.image_processing;
import android.graphics.Bitmap;
import android.graphics.Point;

public interface ImageProcessingFacade {
    public void detectCorners(Bitmap bm, DetectCornersConsumer consumer);
    public Bitmap transformToRectangle(Bitmap bitmap, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft);
    public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer);
    public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer, int[] leftMostXs, int[] upperMostYs);
    public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer);
}


