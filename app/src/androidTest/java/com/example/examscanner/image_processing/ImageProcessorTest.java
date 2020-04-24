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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;

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
        assertEquals(transformed.getHeight(), 233);
        assertEquals(transformed.getWidth(), 338);
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
        assertEquals(transformed.getHeight(), 270);
        assertEquals(transformed.getWidth(), 314);
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
        assertEquals(transformed.getHeight(), 158);
        assertEquals(transformed.getWidth(), 313);

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
        assertEquals(transformed.getHeight(), 90);
        assertEquals(transformed.getWidth(), 137);

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
        assertEquals(transformed.getWidth(), bm.getWidth());
        assertEquals(transformed.getHeight(), bm.getHeight());

    }


    @Test
    public void findQuestions2TestInputContainsOneTemplate(){
        ArrayList<Integer> answers = new ArrayList<>(
                Arrays.asList(4));
        Mat exam_1_q = loadFromResource(R.drawable.exam_1_q);
        Map<Point,Integer> answersMap = imageProcessor.findQuestions(exam_1_q, questionTemplate);
        assertEquals(answersMap.size(), 1);
        assertEquals(new ArrayList<>(answersMap.values()), answers);
    }

    @Test
    public void findQuestions2TestOneColExam(){
        ArrayList<Integer> answers = new ArrayList<>(
                Arrays.asList(4, 3, 4, 3, 5, 1 ,5, 2, 1, 3, 4, 1, 5, 2, 4, 1, 5, 5, 1, 2, 1 ,2, 1, 4, 3));
        Mat exam_25_q = loadFromResource(R.drawable.exam_25_q);
        Map<Point,Integer> answersMap = imageProcessor.findQuestions(exam_25_q, questionTemplate);
        assert answersMap.size() == 25 && answersMap.values().equals(answers);
    }

    @Test
    public void findQuestions2TestTwoColsExam(){
        ArrayList<Integer> answers = new ArrayList<Integer>(
                Arrays.asList(4, 3, 4, 3, 5, 1 ,5, 2, 1, 3, 4, 1, 5, 2, 4, 1, 5, 5, 1, 2, 1 ,2, 1, 4, 3,
                        4, 3, 4, 3, 2, 2, 4, 2, 3 ,2 ,2 ,1 ,1, 2 ,1 ,1, 4, 1 ,3, 4 ,5, 4 ,3 ,5 ,2));
        Mat exam_50_q = loadFromResource(R.drawable.exam_50_q);
        Map<Point,Integer> answersMap = imageProcessor.findQuestions(exam_50_q, questionTemplate);
        assert answersMap.size() == 50 && answersMap.values().equals(answers);
    }

    @Test
    public void findQuestions2TestInputWithoutATemplate(){
        Mat noTemplate = loadFromResource(R.drawable.example_01);
        Map<Point,Integer> answersMap = imageProcessor.findQuestions(noTemplate, questionTemplate);
        assertEquals(answersMap.size(), 0);
    }

    @Test
    public void findQuestions2TestThreeColsExam(){
        ArrayList<Integer> answers = new ArrayList<Integer>(
                Arrays.asList(4, 3, 4, 3, 5, 1 ,5, 2, 1, 3, 4, 1, 5, 2, 4, 1, 5, 5, 1, 2, 1 ,2, 1, 4, 3,
                        4, 3, 4, 3, 2, 2, 4, 2, 3 ,2 ,2 ,1 ,1, 2 ,1 ,1, 4, 1 ,3, 4 ,5, 4 ,3 ,5 ,2));
        Mat exam_3_cols = loadFromResource(R.drawable.exam_3_cols);
        Map<Point,Integer> answersMap = imageProcessor.findQuestions(exam_3_cols, questionTemplate);
        assert answersMap.size() == 50 && answersMap.values().equals(answers);
    }

    @Test
    public void findQuestions2TestUnbalancedColsExam(){}

    @Test
    public void findQuestions3TestInputWithoutATemplate(){
        Mat noTemplate = loadFromResource(R.drawable.example_01);
        Map<Point,Integer> answersMap = imageProcessor.findQuestions(noTemplate, questionTemplate, 0);
        assertEquals(answersMap.size(), 0);
    }

    @Test
    public void findQuestions3TestInputContainsOneTemplate(){
        ArrayList<Integer> answers = new ArrayList<>(
                Arrays.asList(4));
        Mat exam_1_q = loadFromResource(R.drawable.exam_1_q);
        Map<Point,Integer> answersMap = imageProcessor.findQuestions(exam_1_q, questionTemplate, 1);
        assertEquals(answersMap.size(), 1);
        assertEquals(new ArrayList<>(answersMap.values()), answers);
    }

    @Test
    public void findQuestions3TestInputContainsMoreThanOneTemplate(){}

    @Test
    public void findQuestions3TestOneColExam(){
        ArrayList<Integer> answers = new ArrayList<>(
                Arrays.asList(4, 3, 4, 3, 5, 1 ,5, 2, 1, 3, 4, 1, 5, 2, 4, 1, 5, 5, 1, 2, 1 ,2, 1, 4, 3));
        Mat exam_25_q = loadFromResource(R.drawable.exam_25_q);
        Map<Point,Integer> answersMap = imageProcessor.findQuestions(exam_25_q, questionTemplate, 25);
//        assertEquals(answersMap.size(), 25);
//        assertEquals(new ArrayList<>(answersMap.values()), answers);
        assert answersMap.size() == 25 && answersMap.values().equals(answers);
    }

    @Test
    public void findQuestions3TestTwoColsExam(){}

    @Test
    public void findQuestions3TestThreeColsExam(){}

    @Test
    public void findQuestions3TestUnbalancedColsExam(){}

    @Test
    public void findQuestions5TestInputContainsOneTemplate(){
        ArrayList<Integer> answers = new ArrayList<>(
                Arrays.asList(4));
        Mat exam_1_q = loadFromResource(R.drawable.exam_1_q);
        int[] leftMostXs = {268};
        int[] upperMostYs = {79};
        Map<Point,Integer> answersMap = imageProcessor.findQuestions(exam_1_q, questionTemplate, leftMostXs, upperMostYs);
        assertEquals(answersMap.size(), 1);
        assertEquals(new ArrayList<>(answersMap.values()), answers);

    }

    @Test
    public void findQuestions5TestOneColExam(){ }

    @Test
    public void findQuestions5TestTwoColsExam(){}

    @Test
    public void findQuestions5TestInputWithoutATemplate(){}

    @Test
    public void findQuestions5TestThreeColsExam(){}

    @Test
    public void findQuestions5TestUnbalancedColsExam(){}

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
    public void cornerDetectionTestWithRotatedRectangle(){}

    @Test
    public void cornerDetectionTestWithCircle(){}

    @Test
    public void cornerDetectionTestWith2Rectangles(){}

    @Test
    public void cornerDetectionTestImageWithNoBounds(){}

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