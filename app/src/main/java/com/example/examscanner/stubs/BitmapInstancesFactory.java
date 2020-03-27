package com.example.examscanner.stubs;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.examscanner.MainActivity;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class BitmapInstancesFactory {
    public BitmapInstancesFactory(Context context) {
        this.context = context;
    }
    private static final String testJpg1FilePath = "test_jpg_1.jpg";
    private static final String testJpg2FilePath = "test_jpg_2.jpg";
    private static final String testJpg3FilePath = "test_jpg_3.jpg";
    private static final String testTemplate1FilePath = "test_template_1.jpg";
    private static final String testJpg1MarkedFilePath = "test_jpg_1_marked.jpg";
    private static final String testJpg2MarkedFilePath = "test_jpg_2_marked.jpg";
    private static final String testJpg3MarkedFilePath = "test_jpg_3_marked.jpg";
    private Context context;
    public  Bitmap getTestJpg1(){
        return getBitmapFromAssets(testJpg1FilePath);
    }
    public Bitmap getTestJpg2() {
        return getBitmapFromAssets(testJpg2FilePath);
    }
    public Bitmap getTestJpg3() {
        return getBitmapFromAssets(testJpg3FilePath);
    }
    public Bitmap getTestTemplate1() {
        return getBitmapFromAssets(testTemplate1FilePath);
    }
    public  Bitmap getTestJpg1Marked() {
        return getBitmapFromAssets(testJpg1MarkedFilePath);
    }

    public  Bitmap getTestJpg2Marked() {
        return getBitmapFromAssets(testJpg2MarkedFilePath);
    }

    public Bitmap getTestJpg3Marked() {
        return getBitmapFromAssets(testJpg3MarkedFilePath);
    }

    @Nullable
    private Bitmap getBitmapFromAssets(String path) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(path);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    private static int c = 0;
    public Bitmap getRandom() {
        if(c++%3==0)return getTestJpg1Marked();
        if(c++%3==1)return getTestJpg2Marked();
        if(c++%3==2)return getTestJpg3Marked();
        return null;
    }
}
