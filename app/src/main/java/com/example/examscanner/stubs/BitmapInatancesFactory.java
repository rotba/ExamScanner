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

public class BitmapInatancesFactory {
    private static final String testJpg1FilePath = "test_jpg_1.jpg";
    private static final String testJpg2FilePath = "test_jpg_2.jpg";
    private static final String testJpg3FilePath = "test_jpg_3.jpg";
    private static Context context;
    public static Bitmap getTestJpg1(){
        return getBitmapFromAssets(testJpg1FilePath);
    }

    public static Bitmap getTestJpg2() {
        return getBitmapFromAssets(testJpg2FilePath);
    }
    public static Bitmap getTestJpg3() {
        return getBitmapFromAssets(testJpg3FilePath);
    }

    @Nullable
    private static Bitmap getBitmapFromAssets(String path) {
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

    public static void setContext(MainActivity mainActivity) {
        context= mainActivity;
    }

    public static Bitmap getRandom() {
        return getTestJpg1();
//        double random = Math.random();
//        if(random<0.33)return getTestJpg1();
//        if(random<0.66)return getTestJpg2();
//        return getTestJpg3();
    }
}
