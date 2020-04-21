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
import java.util.Map;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

@RunWith(AndroidJUnit4.class)
public class ImageProcessorTest {

    ImageProcessor imageProcessor;
    Mat pdfTest;
    Mat questionTemplate;

    @Before
    public void setUp() {
        OpenCVLoader.initDebug();
        imageProcessor = new ImageProcessor();
        pdfTest = loadFromResource(R.drawable.exam);
        questionTemplate = loadFromResource(R.drawable.template);
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
    public void transformToRectangleTestWithImageRotatedToLeft(){
        Mat example_01 = loadFromResource(R.drawable.example_01);
        Bitmap bm = bitmapFromMat(example_01);
        Bitmap transformed = imageProcessor.transformToRectangle(bm,
                new android.graphics.Point(73,239),
                new android.graphics.Point(356, 117),
                new android.graphics.Point(475, 265),
                new android.graphics.Point(187, 443));
        assert transformed.getHeight() == 233 && transformed.getWidth() == 338;
    }

    @Test
    public void transformToRectangleTestWithImageRotatedToLeft2(){
        Mat example_02 = loadFromResource(R.drawable.example_02);
        Bitmap bm = bitmapFromMat(example_02);
        Bitmap transformed = imageProcessor.transformToRectangle(bm,
                new android.graphics.Point(101,185),
                new android.graphics.Point(393, 151),
                new android.graphics.Point(479, 323),
                new android.graphics.Point(187, 441));
        assert transformed.getHeight() == 270 && transformed.getWidth() == 314;
    }

    @Test
    public void transformToRectangleTestWithImageRotatedToRight(){
        Mat example_03 = loadFromResource(R.drawable.example_03);
        Bitmap bm = bitmapFromMat(example_03);
        Bitmap transformed = imageProcessor.transformToRectangle(bm,
                new android.graphics.Point(63,242),
                new android.graphics.Point(291, 110),
                new android.graphics.Point(361, 252),
                new android.graphics.Point(78, 386));
        assert transformed.getHeight() == 118 && transformed.getWidth() == 313;

    }

    @Test
    public void transformToRectangleTestWithImageRotatedToRight2(){
        Mat example_04 = loadFromResource(R.drawable.example_04);
        Bitmap bm = bitmapFromMat(example_04);
        Bitmap transformed = imageProcessor.transformToRectangle(bm,
                new android.graphics.Point(176,109),
                new android.graphics.Point(272, 11),
                new android.graphics.Point(335, 76),
                new android.graphics.Point(239, 172));
        assert transformed.getHeight() == 90 && transformed.getWidth() == 137;

    }

    @Test
    public void transformToRectangleTestWithImageNotRotated(){
        Mat example_05 = loadFromResource(R.drawable.exam);
        Bitmap bm = bitmapFromMat(example_05);
        Bitmap transformed = imageProcessor.transformToRectangle(bm,
                new android.graphics.Point(0,0),
                new android.graphics.Point(bm.getWidth(), 0),
                new android.graphics.Point(bm.getWidth(), bm.getHeight()),
                new android.graphics.Point(0, bm.getHeight()));
        assert transformed.equals(bm);
    }

    @Test
    public void cornerDetectionTestWithRectangle(){
        OpenCVLoader.initDebug();
        Mat examZoomOut = loadFromResource(R.drawable.exam_zoom_out);
        Imgproc.cvtColor(examZoomOut, examZoomOut, Imgproc.COLOR_BGR2GRAY);
        List<Point> points = imageProcessor.cornerDetection(examZoomOut);
        List<Point> filtered = imageProcessor.removePoints(points);
        List<Point> clockwiseOrderedPoints = imageProcessor.orderPoints(filtered);
        Point upperLeft = clockwiseOrderedPoints.get(1);
        Point upperRight = clockwiseOrderedPoints.get(2);
        Point bottomRight = clockwiseOrderedPoints.get(3);
        Point bottomLeft = clockwiseOrderedPoints.get(0);


    }

    @Test
    public void findQuestions2TestInputContainsOneTemplate(){
        Mat exam_1_q = loadFromResource(R.drawable.exam_1_q);
        Map<Point,Integer> answersMap = imageProcessor.findQuestions(exam_1_q, questionTemplate);
        assert transformed.equals(bm);
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
