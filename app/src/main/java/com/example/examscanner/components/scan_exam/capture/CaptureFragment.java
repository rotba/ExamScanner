package com.example.examscanner.components.scan_exam.capture;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.camera.core.ImageCapture;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.examscanner.R;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;


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
                                processRequestDisposableContainer.add(
                                        Completable.fromCallable(()->{captureViewModel.processCapture();return "Done";})
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeWith(new DisposableCompletableObserver(){
                                            @Override
                                            public void onComplete() {
                                                captureViewModel.postProcessCapture();
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                Log.d(TAG, "captureViewModel.processCapture() with ");
                                                e.printStackTrace();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cameraManager.onDestroy();
    }
}
