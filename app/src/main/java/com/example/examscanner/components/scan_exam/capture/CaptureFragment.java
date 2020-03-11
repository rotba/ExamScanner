package com.example.examscanner.components.scan_exam.capture;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.examscanner.R;
import com.google.common.util.concurrent.ListenableFuture;

import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class CaptureFragment extends Fragment  {
    private CameraManager cameraManager;
    private CameraPermissionRequester permissionRequester;
    private CaptureViewModel captureViewModel;
    private final CompositeDisposable processRequestDisposableContainer = new CompositeDisposable();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_capture, container, false);
        captureViewModel =
                ViewModelProviders.of(this).get(CaptureViewModel.class);
        captureViewModel.getNumOfTotalCaptures().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer totalCaptures) {
                ((TextView)root.findViewById(R.id.capture_processing_progress))
                        .setText(
                                captureViewModel.getNumOfProcessedCaptures().getValue()+"/"+totalCaptures
                        );
            }
        });
        captureViewModel.getNumOfProcessedCaptures().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer processedCaptures) {
                ((TextView)root.findViewById(R.id.capture_processing_progress))
                        .setText(
                                processedCaptures+"/"+captureViewModel.getNumOfTotalCaptures().getValue()
                        );
            }
        });
        cameraManager = new CameraManager(
                getActivity(),
                root
        );
        permissionRequester = new CameraPermissionRequester(
                ()-> cameraManager.setUp(),
                getActivity()
        );
        permissionRequester.request();
        return root;
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConstraintLayout container = (ConstraintLayout)view;
        View v = View.inflate(requireContext(), R.layout.camera_ui_container, container);
        ((AppCompatImageButton)v.findViewById(R.id.capture_image_button))
                .setOnClickListener(cameraManager.getClickLIstener(
                        new Handler(Looper.getMainLooper()){
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                captureViewModel.consumeCapture(new Capture(((ImageCapture.OutputFileResults)msg.obj)));
                                processRequestDisposableContainer.add(Flowable.fromCallable(()->{
                                    captureViewModel.processCapture();
                                    return "hey";
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        captureViewModel.postProcessCapture();
                                    }
                                })
                                );
                            }
                        }
                ));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        permissionRequester.onRequestPermissionsResult(requestCode,permissions, grantResults);
    }
}
