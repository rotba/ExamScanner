package com.example.examscanner;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ScanAnswersConsumer;

public class ImageProcessorsGenerator {
    public static ImageProcessingFacade nullIP(){
        return new ImageProcessingFacade() {
            @Override
            public void detectCorners(Bitmap bm, DetectCornersConsumer consumer) {
                consumer.consume(null,null,null,null);
            }

            @Override
            public Bitmap transformToRectangle(Bitmap bitmap, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft) {
                return bitmap;
            }

            @Override
            public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer) {
                consumer.consume(0,null,null,null,null,null,null);
            }

            @Override
            public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer) {
                consumer.consume(0,null,null,null,null,null,null);
            }
        };
    }
    public static ImageProcessingFacade slowIP(){
        return new ImageProcessingFacade() {
            @Override
            public void detectCorners(Bitmap bm, DetectCornersConsumer consumer) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                consumer.consume(null,null,null,null);
            }

            @Override
            public Bitmap transformToRectangle(Bitmap bitmap, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                consumer.consume(0,null,null,null,null,null,null);
            }

            @Override
            public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                consumer.consume(0,null,null,null,null,null,null);
            }
        };
    }
}
