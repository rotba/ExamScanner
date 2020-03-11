package com.example.examscanner.components.scan_exam.capture;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.example.examscanner.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class CameraManager implements CameraXConfig.Provider{
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private FragmentActivity activity;
    private ImageCapture imageCapture;
    private Handler captureHandler;
    private View root;

    public CameraManager(FragmentActivity activity, Handler captureHandler, View root) {
        this.activity = activity;
        this.captureHandler = captureHandler;
        this.root = root;
    }

    public void setUp() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(activity);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(activity));
    }
    @SuppressLint("RestrictedApi")
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .setTargetName("Preview")
                .build();
        PreviewView previewView = (PreviewView)root.findViewById(R.id.preview_view);
        preview.setSurfaceProvider(previewView.getPreviewSurfaceProvider());
        imageCapture =
                new ImageCapture.Builder()
                        .setTargetRotation(activity.getWindowManager().getDefaultDisplay().getRotation())
                        .build();


        CameraSelector cameraSelector =
                new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        cameraProvider.bindToLifecycle((LifecycleOwner)activity, cameraSelector, preview, imageCapture);
    }
    public View.OnClickListener getClickLIstener(){
        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(new File(activity.getFilesDir(), "filename")).build();
        ImageCapture.OutputFileOptions finalOutputFileOptions = outputFileOptions;
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageCapture.takePicture(
                        finalOutputFileOptions,
                        Executors.newSingleThreadExecutor(),
                        new ImageCapture.OnImageSavedCallback(){
                            @RequiresApi(api = Build.VERSION_CODES.P)
                            @Override
                            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                                Message completeMessage =
                                        captureHandler.obtainMessage(-1, outputFileResults);
                                completeMessage.sendToTarget();
                            }

                            @Override
                            public void onError(@NonNull ImageCaptureException exception) {
                                System.out.println("asd");
                            }
                        }

                );
            }
        };
    }
    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }
}
