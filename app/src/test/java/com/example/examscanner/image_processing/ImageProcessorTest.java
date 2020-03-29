package com.example.examscanner.image_processing;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCaptureRepositoryFacrory;
import com.example.examscanner.stubs.BitmapInatancesFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opencv.android.OpenCVLoader;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;


public class ImageProcessorTest {


    private ImageProcessingFacade imgProcessor = new ImageProcessingFactory().create();

    @Before
    public void setUp() {
        OpenCVLoader.initDebug();
    }

    //TODO: complete implementation and write tests for filterPoints and sortQuestions
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
    public void orderPointsTestWithAlreadyOrderedPoints(){}

    @Test
    public void orderPointsTestWithNot4ElementsArray(){}

    @Test
    public void orderPointsTestWithPointsWithSameX(){}

    @Test
    public void orderPointsTestWithPointsWithSameY(){}

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

    @Test
    public void splitImageTestZeroChunks(){}

    @Test
    public void splitImageTestOneChunks(){}

    @Test
    public void splitImageTestTwoChunks(){}

    @Test
    public void splitImageTestTenChunks(){}

    @Test
    public void cornerDetectionTestWithRectangle(){}

    @Test
    public void cornerDetectionTestWithRotatedRectangle(){}

    @Test
    public void cornerDetectionTestWithCircle(){}

    @Test
    public void cornerDetectionTestWith2Rectangles(){}

    @Test
    public void cornerDetectionTestImageWithNoBounds(){}


}