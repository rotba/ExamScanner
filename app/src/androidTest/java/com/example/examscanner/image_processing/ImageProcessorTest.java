package com.example.examscanner.image_processing;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

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
        pdfTest = loadFromResource();
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

    private Mat loadFromResource() {

        Mat img = null;
        try {
            img = Utils.loadResource(getApplicationContext(), R.drawable.exam);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }

    private Bitmap loadFromAssets(String filename) {

        Bitmap bitmap = null;
        try {
            InputStream ims = getApplicationContext().getAssets().open(filename);
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
