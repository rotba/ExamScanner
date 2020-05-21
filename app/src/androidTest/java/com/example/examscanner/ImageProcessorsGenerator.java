package com.example.examscanner;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.reslove_answers.ScannedCapturesInstancesFactory;
import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ScanAnswersConsumer;

public class ImageProcessorsGenerator {
    public static ImageProcessingFacade nullIP(){
        return new ImageProcessingFacade() {
            @Override
            public void detectCorners(Bitmap bm, DetectCornersConsumer consumer) {
                consumer.consume(new PointF(),null,null,new PointF());
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
            public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer, int[] leftMostXs, int[] upperMostYs) {

            }

            @Override
            public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer) {
                consumer.consume(0,null,null,null,null,null,null);
            }

            @Override
            public Bitmap align(Bitmap bitmap, Bitmap perfectExamImg) {
                return bitmap;
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
                consumer.consume(new PointF(0,0),new PointF(0,0),new PointF(0,0),new PointF(0,0));
            }

            @Override
            public Bitmap transformToRectangle(Bitmap bitmap, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return BitmapsInstancesFactoryAndroidTest.getTestJpg1();
            }

            @Override
            public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ScannedCapturesInstancesFactory.instance1(consumer);
            }

            @Override
            public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer, int[] leftMostXs, int[] upperMostYs) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ScannedCapturesInstancesFactory.instance1(consumer);
            }

            @Override
            public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ScannedCapturesInstancesFactory.instance1(consumer);
            }

            @Override
            public Bitmap align(Bitmap bitmap, Bitmap perfectExamImg) {
                return bitmap;
            }
        };
    }
    public static ImageProcessingFacade quickIP(){
        return new ImageProcessingFacade() {
            @Override
            public void detectCorners(Bitmap bm, DetectCornersConsumer consumer) {
                consumer.consume(new PointF(),null,null,new PointF());
            }

            @Override
            public Bitmap transformToRectangle(Bitmap bitmap, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft) {
                return BitmapsInstancesFactoryAndroidTest.getRandom();
            }

            @Override
            public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer) {
                ScannedCapturesInstancesFactory.instance1(consumer);
            }

            @Override
            public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer, int[] leftMostXs, int[] upperMostYs) {

            }

            @Override
            public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer) {
                ScannedCapturesInstancesFactory.instance1(consumer);
            }

            @Override
            public Bitmap align(Bitmap bitmap, Bitmap perfectExamImg) {
                return null;
            }
        };
    }

    public static ImageProcessingFacade fakeIP(){
        return new ImageProcessingFacade() {
            @Override
            public void detectCorners(Bitmap bm, DetectCornersConsumer consumer) {
                consumer.consume(new PointF(0,0),new PointF(0,0),new PointF(0,0),new PointF(0,0));
            }

            @Override
            public Bitmap transformToRectangle(Bitmap bitmap, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft) {
                return bitmap;
            }

            @Override
            public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer) {
                ScannedCapturesInstancesFactory.instance1(consumer);
            }

            @Override
            public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer, int[] leftMostXs, int[] upperMostYs) {
                ScannedCapturesInstancesFactory.instance1(consumer);
            }

            @Override
            public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer) {
                ScannedCapturesInstancesFactory.instance1(consumer);
            }

            @Override
            public Bitmap align(Bitmap bitmap, Bitmap perfectExamImg) {
                return bitmap;
            }
        };
    }
}
