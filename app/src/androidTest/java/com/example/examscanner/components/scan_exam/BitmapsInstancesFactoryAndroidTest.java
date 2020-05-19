package com.example.examscanner.components.scan_exam;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;

import androidx.test.platform.app.InstrumentationRegistry;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BitmapsInstancesFactoryAndroidTest {
    private static final String testJpg1FilePath = "test_jpg_1.jpg";
    private static final String testJpg2FilePath = "test_jpg_2.jpg";
    private static final String testJpg3FilePath = "test_jpg_3.jpg";
    private static final String testJpg1MarkedFilePath = "test_jpg_1_marked.jpg";
    private static final String testJpg2MarkedFilePath = "test_jpg_2_marked.jpg";
    private static final String testJpg3MarkedFilePath = "test_jpg_3_marked.jpg";
    private static final String testJpgReal1 = "test_jpg_real1.jpg";
    private static final String testJpgReal2 = "test_jpg_real2.jpg";
    private static final String testJpegReal1 = "test_jpg_real1.jpeg";
    private static  final String getTestJpegDiagonal1 = "test_jpeg_diagonal1.jpeg";
    private static  final String getTestJpegBlackBackgroud = "black_back.jpg";
    private static final String testAuthPic1= "test_auth_pic_1.jpg";
    private static final String test50Qs= "exam_50_q.jpg";
    private static final String test50QsAuth= "exam_50_q_auth.jpg";
    private static String Comp191_V1_JPG_Auth_No_Flash = "comp191_v1_jpg_auth.jpg";
    private static String Comp191_V1_PDF_Auth_No_Flash = "comp191_v1_pdf_auth.jpg";;
    private static String Comp191_v1_JPG_ANS = "comp191_v1_ans.jpg";
    private static String Comp191_v1_JPG_ANS_2 = "comp191_v1_ans_jpg_2.jpg";

    public static Bitmap getTestJpg1() {
        return getBitmapFromAssets(testJpg1FilePath);
    }

    public static Bitmap getTestJpg2() {
        return getBitmapFromAssets(testJpg2FilePath);
    }

    public static Bitmap getTestJpg3() {
        return getBitmapFromAssets(testJpg3FilePath);
    }

    public static Bitmap getTestJpgReal1() {
        return getBitmapFromAssets(testJpgReal1);
    }
    public static Bitmap getTestJpgReal2() {
        return getBitmapFromAssets(testJpgReal2);
    }




    public static Bitmap getTestJpegReal1() {
        return getBitmapFromAssets(testJpegReal1);
    }
    public static Bitmap getTestJpgBlackBack() {
        return getBitmapFromAssets(getTestJpegBlackBackgroud);
    }

    public static Bitmap getTestJpg1Marked() {
        return getBitmapFromAssets(testJpg1MarkedFilePath);
    }

    public static Bitmap getTestJpg2Marked() {
        return getBitmapFromAssets(testJpg2MarkedFilePath);
    }

    public static Bitmap getTestJpg3Marked() {
        return getBitmapFromAssets(testJpg3MarkedFilePath);
    }

    public static Bitmap getTestJpgDiagonal1() {
        return getBitmapFromAssets(getTestJpegDiagonal1);
    }

    public static Bitmap getExam50Qs() { return getBitmapFromAssets(test50Qs); }

    public static Bitmap getComp191_V1_JPG_Auth_No_Flash() {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);
        final Bitmap bitmapFromAssets = getBitmapFromAssets(Comp191_V1_JPG_Auth_No_Flash);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                bitmapFromAssets,
                bitmapFromAssets.getWidth(),
                bitmapFromAssets.getHeight(),
                true
        );


        return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }

    public static Bitmap getComp191_V1_PDF_Auth_No_Flash() {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);
        final Bitmap bitmapFromAssets = getBitmapFromAssets(Comp191_V1_PDF_Auth_No_Flash);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                bitmapFromAssets,
                bitmapFromAssets.getWidth(),
                bitmapFromAssets.getHeight(),
                true
        );


        return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }

    public static Bitmap getComp191_v1_JPG_ANS() {
        return getBitmapFromAssets(Comp191_v1_JPG_ANS);
    }

    public static Bitmap getComp191_v1_JPG_ANS_2() {
        return getBitmapFromAssets(Comp191_v1_JPG_ANS_2);
    }

    public static Bitmap getExam50QsAuth() {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);
        final Bitmap bitmapFromAssets = getBitmapFromAssets(test50QsAuth);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                bitmapFromAssets,
                bitmapFromAssets.getWidth(),
                bitmapFromAssets.getHeight(),
                true
        );


        return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }

    public static Bitmap getTestAuthPic1Marked() {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);

        final Bitmap bitmapFromAssets = getBitmapFromAssets(testAuthPic1);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                bitmapFromAssets,
                bitmapFromAssets.getWidth(),
                bitmapFromAssets.getHeight(),
                true
        );


        return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }

    @Nullable
    private static Bitmap getBitmapFromAssets(String path) {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(path);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }

    private static int c = 0;

    public static Bitmap getRandom() {
        int curr_c = c++;
        if (curr_c % 3 == 0) return getTestJpg1Marked();
        if (curr_c % 3 == 1) return getTestJpg2Marked();
        if (curr_c % 3 == 2) return getTestJpg3Marked();
        return null;
    }
}

