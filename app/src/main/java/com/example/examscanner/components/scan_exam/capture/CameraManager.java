package com.example.examscanner.components.scan_exam.capture;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CameraManager implements CameraXConfig.Provider{
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private FragmentActivity activity;
    private ImageCapture imageCapture;
    private View root;
    private Executor executor;

    public CameraManager(FragmentActivity activity, View root) {
        this.activity = activity;
        this.root = root;
        executor = ContextCompat.getMainExecutor(activity);
    }

    public void setUp() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(activity);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.d(TAG, "setUp()");
                e.printStackTrace();
            }
        }, executor);
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
        cameraProvider.bindToLifecycle(activity, cameraSelector, preview, imageCapture);
    }

    public View.OnClickListener getClickLIstener(Handler handler){
        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(new File(activity.getFilesDir(), "filename")).build();
        ImageCapture.OutputFileOptions finalOutputFileOptions = outputFileOptions;
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageCapture.takePicture(
                        finalOutputFileOptions,
                        executor,
                        new ImageCapture.OnImageSavedCallback(){
                            @RequiresApi(api = Build.VERSION_CODES.P)
                            @Override
                            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                                Message completeMessage =
                                        handler.obtainMessage(-1, outputFileResults);
                                completeMessage.sendToTarget();
                            }

                            @SuppressLint("RestrictedApi")
                            @Override
                            public void onError(@NonNull ImageCaptureException exception) {
                                Log.d(TAG, "imageCapture.takePicture()");
                                CameraManager.this.onDestroy();
                                exception.printStackTrace();
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
    @SuppressLint("RestrictedApi")
    public void onDestroy(){
        cameraProviderFuture = ProcessCameraProvider.getInstance(activity);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraProvider.shutdown();
            } catch (ExecutionException | InterruptedException e) {
                Log.d(TAG, "onPause()");
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(activity));
    }
    public void onPause(){
        cameraProviderFuture = ProcessCameraProvider.getInstance(activity);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                unbind(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.d(TAG, "onPause()");
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(activity));
    }

    private void unbind(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
    }
}
