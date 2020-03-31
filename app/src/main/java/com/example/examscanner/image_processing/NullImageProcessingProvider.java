package com.example.examscanner.image_processing;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import com.example.examscanner.MainActivity;

import java.io.File;
import java.io.IOException;

import static android.os.Environment.DIRECTORY_PICTURES;


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
    public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer, int[] leftMostXs, int[] upperMostYs) {

    }

    @Override
    public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer) {
        consumer.consume(0,null,null,null,null,null,null);
    }

    @Override
    public void detectCorners(Bitmap bm, DetectCornersConsumer consumer) {
        consumer.consume(null,null,null,null);
    }
}
