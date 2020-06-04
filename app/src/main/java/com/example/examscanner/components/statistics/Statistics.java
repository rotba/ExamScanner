package com.example.examscanner.components.statistics;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.examscanner.repositories.exam.Exam;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Statistics implements Runnable {
    private static final String TAG = "ExamScanner";
    private static final String MSG_PREF = "Statistics";
    private Exam e;
    private MutableLiveData<Double> mAvg;
    private MutableLiveData<Double> mVar;
    private MutableLiveData<Integer> mNumOfSolutions;

    public LiveData<Double> getAvg() {
        return mAvg;
    }

    public LiveData<Double> getVar() {
        return mVar;
    }

    public LiveData<Integer> getNumOfSolutions() {
        return mNumOfSolutions;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            StatisticsHolder sh = new StatisticsHolder();
            Completable.fromAction(()->{
                sh.avg = e.getAvg();
                sh.var = e.getVar();
                sh.numOfSoltuions = e.getCheckedSoltuions();
            }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    ()->{mAvg.setValue(sh.avg);mVar.setValue(sh.var);mNumOfSolutions.setValue(sh.numOfSoltuions);},
                    t-> Log.d(TAG, MSG_PREF,t)
            );
        }
    }
    private class StatisticsHolder{
        double avg;
        double var;
        int numOfSoltuions;
    }
}
