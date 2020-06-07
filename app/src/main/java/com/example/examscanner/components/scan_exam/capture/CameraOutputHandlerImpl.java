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
    private OnBeggining cont;
    private OnEnding onEnd;

    public CameraOutputHandlerImpl(CaptureViewModel captureViewModel, CompositeDisposable processRequestDisposableContainer, OnBeggining cont, OnEnding onEnd) {
        this.captureViewModel = captureViewModel;
        this.processRequestDisposableContainer = processRequestDisposableContainer;
        this.cont = cont;
        this.onEnd = onEnd;
    }

    @Override
    public View.OnClickListener handleBitmap(Bitmap bm) {
        captureViewModel.consumeCapture(
                new Capture(bm,
                        captureViewModel.getCurrentExamineeId().getValue(),
                        captureViewModel.getCurrentVersion().getValue()
                )
        );
        cont.cont();
        processRequestDisposableContainer.add(
                Completable.fromAction(this::processCapture)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onCaptureProcessed, this::onCapturePtocessError)
        );
        return null;
    }

    private void processCapture() {
        captureViewModel.processCapture();
    }

    private void onCapturePtocessError(Throwable throwable) {
        Log.d(TAG, MSG_PREF, throwable);
        onEnd.cont();
    }

    private void onCaptureProcessed() {
        Completable.fromAction(
                () -> {captureViewModel.postProcessCapture();}
        ).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(()->{}, t->Log.d(TAG, MSG_PREF,t ));
        onEnd.cont();
    }
    public interface OnBeggining {
        public void cont();
    }

    public interface OnEnding {
        public void cont();
    }
}
