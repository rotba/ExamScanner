package com.example.examscanner.components.scan_exam.capture;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.example.examscanner.components.scan_exam.capture.camera.CameraOutputHander;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CameraOutputHandlerImpl implements CameraOutputHander {
    private static String TAG = "ExamScanner";
    private static String MSG_PREF = "CameraOutputHandlerImpl::";
    private CaptureViewModel captureViewModel;
    private final CompositeDisposable processRequestDisposableContainer;

    public CameraOutputHandlerImpl(CaptureViewModel captureViewModel, CompositeDisposable processRequestDisposableContainer) {
        this.captureViewModel = captureViewModel;
        this.processRequestDisposableContainer = processRequestDisposableContainer;
    }

    @Override
    public View.OnClickListener handleBitmap(Bitmap bm) {
        captureViewModel.consumeCapture(new Capture(bm));
        processRequestDisposableContainer.add(
                Completable.fromAction(this::processCapture)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onCaptureProcessed, this::onCapturePtocessError)
        );
        return null;
    }

    private void processCapture(){
        captureViewModel.processCapture();
    }

    private void onCapturePtocessError(Throwable throwable) {
        Log.d(TAG, MSG_PREF, throwable);
        throwable.printStackTrace();
    }

    private void onCaptureProcessed() {
        captureViewModel.postProcessCapture();
    }
}
