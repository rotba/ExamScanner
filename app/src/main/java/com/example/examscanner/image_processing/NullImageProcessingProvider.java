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
    public ICornerDetectionResult detectCorners(Object o) {
        return new ICornerDetectionResult() {
            @Override
            public Bitmap getBitmap() {
                Context context = MainActivity.getContext();
                AssetManager assetManager = context.getAssets();
                try {
                    return BitmapFactory.decodeStream(assetManager.open("temp_test.jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public Bitmap transformToRectangle(Bitmap bitmap, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft) {
        return null;
    }

    @Override
    public Object foo(Object d) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IScannedCapture scanAnswers(Bitmap bitmap) {
        return new IScannedCapture() {
            @Override
            public int getIdentified() {
                return 40;
            }

            @Override
            public int getUnidentified() {
                return 10;
            }

            @Override
            public int[] getAnswers() {
                return new int[0];
            }
        };
    }
}
