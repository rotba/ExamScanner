package com.example.examscanner.communication;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.R;
import com.example.examscanner.image_processing.ImageProcessor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;
import java.util.ArrayList;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

@RunWith(AndroidJUnit4.class)
public class ImageProcessorTest {

    @Test
    public void splitImageTestZeroChunks(){
        OpenCVLoader.initDebug();

        Mat img = null;
        try {
            img = Utils.loadResource(getApplicationContext(), R.drawable.test_jpeg_diagonal1);

        } catch (IOException e) {
            e.printStackTrace();
        }

     //   Mat img = Imgcodecs.imread("unnamed");
        Bitmap bm = Bitmap.createBitmap(img.cols(), img.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img, bm);
        ArrayList<Bitmap> chunks = ImageProcessor.splitImage(bm, 0);
        assert bm.equals(chunks.get(0));
    }
}
