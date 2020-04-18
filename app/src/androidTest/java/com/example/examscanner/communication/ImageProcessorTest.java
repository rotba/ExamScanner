package com.example.examscanner.communication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

@RunWith(AndroidJUnit4.class)
public class ImageProcessorTest {

    Mat pdfTest;

    @Before
    public void setUp() {
        OpenCVLoader.initDebug();
        pdfTest = loadFromResource("test_jpeg_diagonal1");
        // pdfTest = loadFromAssets("test_jpeg_diagonal1");
    }

    private Bitmap bitmapFromMat(Mat mat){
        Bitmap bm = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bm);
        return bm;
    }

    private Mat matFromBitmap(Bitmap bm){
        Mat mat = new Mat();
        Utils.bitmapToMat(bm, mat);
        return mat;
    }

    private Mat loadFromResource(String test_jpeg_diagonal1) {

        Mat img = null;
        try {
            img = Utils.loadResource(getApplicationContext(), R.drawable.test_jpeg_diagonal1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }

    private Bitmap loadFromAssets(String test_jpeg_diagonal1) {

        Bitmap bitmap = null;
        try {
            InputStream ims = getApplicationContext().getAssets().open("test_jpg_1.jpg");
            bitmap = BitmapFactory.decodeStream(ims);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Test
    public void splitImageTestZeroChunks() throws IOException {

        Bitmap bm = bitmapFromMat(pdfTest);
        ArrayList<Bitmap> chunks = ImageProcessor.splitImage(bm, 0);
        assert bm.equals(chunks.get(0));
    }
}
