package com.example.examscanner.stubs;

import com.example.examscanner.persistence.remote.RemoteDatabaseFacade;
import com.example.examscanner.persistence.remote.entities.Grader;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class RemoteDatabaseStubInstance implements RemoteDatabaseFacade {
    @Override
    public Completable createExam(long examId, String courseName, String url, String year, int term, int semester, String mangerId, String[] gradersIdentifiers, boolean seal, long sessionId) {
        return Completable.fromAction(()->{});
    }

    @Override
    public Completable addVersion(long versionId, long examId, int versionNumber) {
        return Completable.fromAction(()->{});
    }

    @Override
    public Observable<List<Grader>> getGraders() {
        return Observable.fromCallable(()->{return new ArrayList<Grader>();});
    }

    @Override
    public Completable createGrader(String userName) {
        return Completable.fromAction(()->{});
    }
}
