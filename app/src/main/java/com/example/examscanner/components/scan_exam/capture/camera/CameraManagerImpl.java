package com.example.examscanner.components.scan_exam.capture.camera;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.capture.CameraOutputHandlerImpl;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import static androidx.constraintlayout.widget.Constraints.TAG;

class CameraManagerImpl implements CameraXConfig.Provider,CameraManager{
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private FragmentActivity activity;
    private ImageCapture imageCapture;
    private View root;
    private Executor executor;

    public CameraManagerImpl(FragmentActivity activity, View root) {
        this.activity = activity;
        this.root = root;
        executor = ContextCompat.getMainExecutor(activity);
    }

    @Override
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

    @Override
    public View.OnClickListener createCaptureClickListener(CameraOutputHander handler){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageCapture.takePicture(
                        executor,
                        new ImageCapture.OnImageCapturedCallback() {
                            @Override
                            public void onCaptureSuccess(@NonNull ImageProxy image) {
                                super.onCaptureSuccess(image);
                                ImageProxy.PlaneProxy[] planes = image.getPlanes();
                                ByteBuffer yBuffer = planes[0].getBuffer();
                                ByteBuffer uBuffer = planes[1].getBuffer();
                                ByteBuffer vBuffer = planes[2].getBuffer();

                                int ySize = yBuffer.remaining();
                                int uSize = uBuffer.remaining();
                                int vSize = vBuffer.remaining();
                                byte[] nv21 = new byte[ySize + uSize + vSize];
                                yBuffer.get(nv21, 0, ySize);
                                vBuffer.get(nv21, ySize, vSize);
                                uBuffer.get(nv21, ySize + vSize, uSize);

                                YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out);

                                byte[] imageBytes = out.toByteArray();
                                handler.handleBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
                                image.close();
                            }

                            @Override
                            public void onError(@NonNull ImageCaptureException exception) {
                                super.onError(exception);
                                Log.d(TAG, "imageCapture.takePicture()");
                                CameraManagerImpl.this.onDestroy();
                                exception.printStackTrace();
                            }
                        }
                );
//                imageCapture.takePicture(
//                        finalOutputFileOptions,
//                        executor,
//                        new ImageCapture.OnImageSavedCallback(){
//                            @RequiresApi(api = Build.VERSION_CODES.P)
//                            @Override
//                            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
//                                Message completeMessage =
//                                        handler.obtainMessage(-1, outputFileResults);
//                                completeMessage.sendToTarget();
//                            }
//
//                            @SuppressLint("RestrictedApi")
//                            @Override
//                            public void onError(@NonNull ImageCaptureException exception) {
//                                Log.d(TAG, "imageCapture.takePicture()");
//                                CameraManager.this.onDestroy();
//                                exception.printStackTrace();
//                            }
//                        }
//
//                );
            }
        };
    }
    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }

    @SuppressLint("RestrictedApi")
    @Override
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
    @Override
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
