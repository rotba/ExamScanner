package com.example.examscanner.communication.tasks;

import android.util.Log;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class Task {
    private final String TAG = "ExamScanner";
    private Completable completable;
    private AtomicBoolean completed;
    private Continuation mainCont;
    private ConcurrentLinkedQueue<Continuation> conts;

    public Task(Completable completable, String desc) {
        this.completable = completable;
        completed = new AtomicBoolean(false);
        conts = new ConcurrentLinkedQueue<>();
        mainCont = () -> {
            while (!conts.isEmpty()) {
                conts.remove().cont();
            }
        };
        completable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(() -> {
            Log.d(TAG, String.format("Task completed with description: %s", desc));
            completed.set(true);
            mainCont.cont();
        }, throwable -> {
            throw new TaskException(throwable);
        });
    }

    public void addContinuation(Continuation continuation, Continuation onVeporized) {
        if (completed.get()) {
            continuation.cont();
        } else {
            conts.add(continuation);
        }
    }

}
