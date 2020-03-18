package com.example.examscanner.components.scan_exam;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.example.examscanner.MainActivity;
import com.example.examscanner.R;
import com.example.examscanner.image_processing.ICornerDetectionResult;
import com.example.examscanner.image_processing.IScannedCapture;
import com.example.examscanner.image_processing.ImageProcessingFacade;

import java.io.IOException;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class Utils {
    public static void navFromHomeToCapture() {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withText(R.string.gallery_button_alt)).perform(click());
        onView(withText(R.string.start_scan_exam)).perform(click());
    }
    public static void navFromCaptureToDetectCorners(){
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
    }
    public static void sleepCameraPreviewSetupTime() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void sleepSwipingTime() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepSingleCaptureTakingTime() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void sleepSingleCaptureProcessingTime() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepMovingFromCaptureToDetectCorners() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepScreenRotationTime(){
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static ImageProcessingFacade TEMP_TESTImageProcessor(){
        return new ImageProcessingFacade() {
            @Override
            public Object foo(Object d) {
                return null;
            }

            @Override
            public ICornerDetectionResult detectCorners(Object o) {
                return new ICornerDetectionResult() {
                    @Override
                    public Bitmap getBitmap() {
                        Context context = MainActivity.getContext();
                        AssetManager assetManager = context.getAssets();
                        try {
                            return BitmapFactory.decodeStream(assetManager.open("temp_test.jpg"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
            }

            @Override
            public Bitmap transformToRectangle(Bitmap bitmap, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft) {
                return null;
            }

            @Override
            public IScannedCapture scanAnswers(Bitmap bitmap) {
                return null;
            }
        };
    }
}
