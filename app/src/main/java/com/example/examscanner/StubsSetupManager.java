package com.example.examscanner;

import android.content.Context;
import android.view.View;

import com.example.examscanner.components.scan_exam.capture.camera.CameraManager;
import com.example.examscanner.components.scan_exam.capture.camera.CameraMangerFactory;
import com.example.examscanner.components.scan_exam.capture.camera.CameraOutputHander;
import com.example.examscanner.stubs.BitmapInstancesFactory;

class StubsSetupManager {
    public static void setup(Context c) {
        CameraMangerFactory.setStubInstance(new CameraManager() {
            @Override
            public void setUp() {

            }

            @Override
            public View.OnClickListener createCaptureClickListener(CameraOutputHander handler) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.handleBitmap(new BitmapInstancesFactory(c).get_1());
                    }
                };
            }

            @Override
            public void onPause() {

            }

            @Override
            public void onDestroy() {

            }
        });
    }
}
