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
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

@RunWith(AndroidJUnit4.class)
public class ImageProcessorTest {

    ImageProcessor imageProcessor;
    Mat pdfTest;

    @Before
    public void setUp() {
        OpenCVLoader.initDebug();
        imageProcessor = new ImageProcessor();
        pdfTest = loadFromResource(R.drawable.exam);
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

    private Mat loadFromResource(int file) {

        Mat img = null;
        try {
            img = Utils.loadResource(getApplicationContext(), file);

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
    public void cornerDetectionTestWithRectangle(){
        OpenCVLoader.initDebug();
        Mat examZoomOut = loadFromResource(R.drawable.exam_zoom_out);
        List<Point> points = imageProcessor.cornerDetection(examZoomOut);
        List<Point> filtered = imageProcessor.removePoints(points);
        List<Point> clockwiseOrderedPoints = imageProcessor.orderPoints(filtered);
        Point upperLeft = clockwiseOrderedPoints.get(1);
        Point upperRight = clockwiseOrderedPoints.get(2);
        Point bottomRight = clockwiseOrderedPoints.get(3);
        Point bottomLeft = clockwiseOrderedPoints.get(0);


    }

    @Test
    public void cornerDetectionTestWithRotatedRectangle(){}

    @Test
    public void cornerDetectionTestWithCircle(){}

    @Test
    public void cornerDetectionTestWith2Rectangles(){}

    @Test
    public void cornerDetectionTestImageWithNoBounds(){}



    @Test
    public void findQuestions2TestInputWithoutATemplate(){}

    @Test
    public void findQuestions2TestInputContainsOneTemplate(){}

    @Test
    public void findQuestions2TestInputContainsMoreThanOneTemplate(){}

    @Test
    public void findQuestions2TestOneColExam(){}

    @Test
    public void findQuestions2TestTwoColsExam(){}

    @Test
    public void findQuestions2TestThreeColsExam(){}

    @Test
    public void findQuestions2TestUnbalancedColsExam(){}

    @Test
    public void findQuestions3TestInputWithoutATemplate(){}

    @Test
    public void findQuestions3TestInputContainsOneTemplate(){}

    @Test
    public void findQuestions3TestInputContainsMoreThanOneTemplate(){}

    @Test
    public void findQuestions3TestOneColExam(){}

    @Test
    public void findQuestions3TestTwoColsExam(){}

    @Test
    public void findQuestions3TestThreeColsExam(){}

    @Test
    public void findQuestions3TestUnbalancedColsExam(){}

    @Test
    public void transformToRectangleTestWithImageRotatedToLeft(){}

    @Test
    public void transformToRectangleTestWithImageRotatedToRight(){}

    @Test
    public void transformToRectangleTestWithImageNotRotated(){}

    @Test
    public void transformToRectangleTestWithUnboundedPoints(){}

    @Test
    public void markedAnswerTestEmptyArray(){}

    @Test
    public void markedAnswerTestWithOnePicture(){}

    @Test
    public void markedAnswerTestWithNothingMarked(){}

    @Test
    public void markedAnswerTestWithTwoMarking(){}

    @Test
    public void markedAnswerTestWithXMarking(){}

    @Test
    public void markedAnswerTestWithNotFullMarking(){}




    // need to test indirectly

    @Test
    public void splitImageTestZeroChunks(){}

    @Test
    public void splitImageTestOneChunks(){}

    @Test
    public void splitImageTestTwoChunks(){}

    @Test
    public void splitImageTestTenChunks(){}

    @Test
    public void orderPointsTestWithAlreadyOrderedPoints(){}

    @Test
    public void orderPointsTestWithNot4ElementsArray(){}

    @Test
    public void orderPointsTestWithPointsWithSameX(){}

    @Test
    public void orderPointsTestWithPointsWithSameY(){}

}
