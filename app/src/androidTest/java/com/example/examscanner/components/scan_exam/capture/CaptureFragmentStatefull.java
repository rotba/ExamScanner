package com.example.examscanner.components.scan_exam.capture;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;

import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;

import com.example.examscanner.R;
import com.example.examscanner.StateFullTest;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.components.scan_exam.Utils.navFromHomeToCapture;
import static com.example.examscanner.components.scan_exam.Utils.sleepCameraPreviewSetupTime;
import static com.example.examscanner.components.scan_exam.Utils.sleepScreenRotationTime;
import static com.example.examscanner.components.scan_exam.Utils.sleepSingleCaptureProcessingTime;
import static com.example.examscanner.components.scan_exam.capture.CaptureUtils.assertUserSeeProgress;

public class CaptureFragmentStatefull extends StateFullTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
        navFromHomeToCapture();

    }
}
