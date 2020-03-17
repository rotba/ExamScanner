package com.example.examscanner.imageProcessor;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class imgProcessor {

    public Map<Point,Integer> findQuestions(Mat img_exam, Mat img_template) {
        int result_cols = img_exam.cols() - img_template.cols() + 1;
        int result_rows = img_exam.rows() - img_template.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1); //CV_32FC1 means 32 bit floating point signed depth in one channel

        // / Do the Matching and Normalize
        Imgproc.matchTemplate(img_exam, img_template, result, Imgproc.TM_CCOEFF_NORMED);
        //     Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());


        Core.MinMaxLocResult mmr;
        Point matchLoc;
        Map<Point,Integer> answersMap = new HashMap<>();

        while(true)
        {
            mmr = Core.minMaxLoc(result);
            // match loc is the left upper point of the found template
            // x + -> right direction
            // y + -> down direction
            matchLoc = mmr.maxLoc;
            // threshold chosen empirically
            if(mmr.maxVal >=0.67)
            {
                Mat img_result = img_exam.clone();
                Imgproc.rectangle(img_exam, matchLoc, new Point(matchLoc.x + img_template.cols(),
                        matchLoc.y + img_template.rows()), new Scalar(0, 0, 255), -1);
                // find the answer
                // part the matching image into 5
                Bitmap img_bitmap = Bitmap.createBitmap(img_result.cols(), img_result.rows(),Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(img_result, img_bitmap);
                Bitmap bm = Bitmap.createBitmap(img_bitmap, (int)matchLoc.x,(int)matchLoc.y, img_template.cols(), img_template.rows());
                ArrayList<Bitmap> imgChunks = splitImage(bm,5);
                int correctAns = markedAnswer(imgChunks);
                answersMap.put(matchLoc, correctAns+1);
                // erase this matching to prevent infinite loop
                Imgproc.rectangle(result, matchLoc, new Point(matchLoc.x + img_template.cols(),
                        matchLoc.y + img_template.rows()), new Scalar(0, 0, 0), -1);

//                Point diagonalPoint = new Point(matchLoc.x + img_template.cols(), matchLoc.y + img_template.rows());
//                Rect rect = new Rect((int)matchLoc.x, (int)matchLoc.y, img_template.cols(), img_template.rows());
//                Mat currAns = img_exam.submat(rect);
//                ArrayList<Bitmap> imgChunks = splitImage(bm,5);
//                int correctAns = markedAnswer(imgChunks);
//                answersMap.put(matchLoc, correctAns);
//                // draw rectangle around the found template in the output picture (red)
//                Imgproc.rectangle(img_exam, matchLoc, diagonalPoint, new Scalar(0, 0, 255), 2);
//                // erase this matching in order to find next matching (black)
//                Imgproc.rectangle(result, matchLoc, diagonalPoint, new Scalar(0, 0, 0), -1);
            }
            else
            {
                break; // No more results within tolerance, break search
            }
        }
        return answersMap;


    }
    // sort points:
    // x value is the most significant
    private Map<Integer, Integer> sortQuestions(Map<Point, Integer> answersMap) {
        class PointComparator implements Comparator {
            public int compare(Object o1,Object o2){
                Point p1=(Point)o1;
                Point p2=(Point)o2;

//                if(a.x < b.x)
//                    return -1;
//                if(a.x > b.x)
//                    return 1;
//                if(a.x == b.x && a.y < b.y)
//                    return -1;
//                if(a.x == b.x && a.y > b.y)
//                    return 1;
//                return 0;


                if(p1.x == p2.x && p1.y == p2.y)
                    return 0;
                else if(p1.x > p2.x)
                    return 1;
                else if(p1.x == p2.x && p1.y > p2.y)
                    return 1;
                else
                    return -1;
            }
        }
        Comparator pointComp = new PointComparator();
        Set<Point> keys = answersMap.keySet();
        ArrayList<Point> keysList = new ArrayList<>(keys);
        Collections.sort(keysList, pointComp);
        Map<Integer, Integer> ret = new HashMap<>();
        int q_num = 1;
        for(Point p : keysList){
            ret.put(q_num, answersMap.get(p));
            q_num++;
        }
        return ret;
    }


    // find out which answer was marked by determining which sub-image in imagesArr
    // has the bigget number of colored pixeles
    private int markedAnswer(ArrayList<Bitmap> imgChunks) {

//        let maxBlack = 0;
//        let maxImg = 0;
//        for(let i = 0; i < imagesArr.length; i++){
//            let currAns = imagesArr[i];
//            //CONVERT IMAGE TO B&W AND DETERMINE IF IT HAS MORE BLACK OR WHITE
//            cv.cvtColor(currAns, currAns, cv.COLOR_BGR2GRAY);
//            let nonBlackPixels = cv.countNonZero(currAns);
//            let blackPixels = currAns.rows * currAns.cols - nonBlackPixels;
//            if(maxBlack < blackPixels){
//                maxBlack = blackPixels;
//                maxImg = i+1;
//            }
//        }
//
//        return maxImg;

        int maxBlack = 0;
        int maxImg = -2;
        for(int i = 0; i < imgChunks.size(); i++){
            Mat currMat = new Mat();
            Bitmap currBitMap = imgChunks.get(i);
            Utils.bitmapToMat(currBitMap, currMat);
            //CONVERT IMAGE TO B&W AND DETERMINE IF IT HAS MORE BLACK OR WHITE
            Imgproc.cvtColor(currMat, currMat, Imgproc.COLOR_BGR2GRAY);
            int non_black_pixels = Core.countNonZero(currMat);
            int black_pixels = currMat.rows() * currMat.cols() - non_black_pixels;
            if(maxBlack < black_pixels){
                maxBlack = black_pixels;
                maxImg = i;
            }
        }

        return maxImg;
    }


    // split source image into chunksNum smaller images
    // split is done according to the image width
    private ArrayList<Bitmap> splitImage(Bitmap bitmap, int chunkNumbers) {


//        let sizeOfPart = image.cols/chunksNum;
//        let chunkedImages = [];
//        for(let i = 0; i < chunksNum; i++){
//            let ith_ans = new cv.Rect(i*sizeOfPart, 0, sizeOfPart, image.rows);
//            let temp = image.roi(ith_ans);
//            chunkedImages.push(temp);
//        }
//        return chunkedImages;

        ArrayList<Bitmap> chunkedImages = new ArrayList<Bitmap>(chunkNumbers);
        int sizeOfPart = bitmap.getWidth()/chunkNumbers;
        for(int i = 0; i < chunkNumbers; i++){
            Bitmap cropped = Bitmap.createBitmap(bitmap,i*sizeOfPart,0,sizeOfPart, bitmap.getHeight());
            chunkedImages.add(cropped);
        }
        return chunkedImages;
    }
}
