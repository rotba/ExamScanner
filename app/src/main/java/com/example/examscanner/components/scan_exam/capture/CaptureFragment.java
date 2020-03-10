package com.example.examscanner.components.scan_exam.capture;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.examscanner.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class CaptureFragment extends Fragment implements CameraXConfig.Provider {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private View root;
    private CaptureViewModel captureViewModel;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        captureViewModel =
                ViewModelProviders.of(this).get(CaptureViewModel.class);
//        View subContainer = inflater.inflate(R.id.camera_ui_container, null);
        root = inflater.inflate(R.layout.fragment_capture, container, false);
        handleCamera();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConstraintLayout container = (ConstraintLayout)view;
        View.inflate(requireContext(), R.layout.camera_ui_container, container);
    }

    private void handleCamera() {
        handleCameraPermission();
    }

    private void setUpCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(getActivity()));
    }

    @SuppressLint("RestrictedApi")
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .setTargetName("Preview")
                .build();
        PreviewView previewView = (PreviewView)root.findViewById(R.id.preview_view);
        preview.setSurfaceProvider(previewView.getPreviewSurfaceProvider());
        ImageCapture imageCapture =
                new ImageCapture.Builder()
                        .setTargetRotation(getActivity().getWindowManager().getDefaultDisplay().getRotation())
                        .build();


        CameraSelector cameraSelector =
                new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview);
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }

    private void handleCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.CAMERA)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                setUpCamera();
            }
        }else{
            setUpCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUpCamera();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
