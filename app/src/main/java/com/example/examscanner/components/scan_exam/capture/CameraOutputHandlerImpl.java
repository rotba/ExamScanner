package com.example.examscanner.components.scan_exam.capture;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.examscanner.components.scan_exam.capture.camera.CameraOutputHander;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CameraOutputHandlerImpl implements CameraOutputHander {
    private CaptureViewModel captureViewModel;
    private final CompositeDisposable processRequestDisposableContainer;

    public CameraOutputHandlerImpl(CaptureViewModel captureViewModel, CompositeDisposable processRequestDisposableContainer) {
        this.captureViewModel = captureViewModel;
        this.processRequestDisposableContainer = processRequestDisposableContainer;
    }

    @Override
    public void handleBitmap(Bitmap bm) {
        captureViewModel.consumeCapture(new Capture(bm));
        processRequestDisposableContainer.add(
                Completable.fromCallable(() -> {
                    captureViewModel.processCapture();
                    return "Done";
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
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
