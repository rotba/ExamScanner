package com.example.examscanner.stubs;

import com.example.examscanner.persistence.remote.RemoteDatabaseFacade;

import io.reactivex.Completable;

public class RemoteDatabaseStubInstance implements RemoteDatabaseFacade {
    @Override
    public Completable createExam(long examId, String courseName, String url, String year, int term, int semester, String mangerId, String[] gradersIdentifiers, boolean seal, long sessionId) {
        return Completable.fromAction(()->{});
    }

    @Override
    public Completable addVersion(long versionId, long examId, int versionNumber) {
        return Completable.fromAction(()->{});
    }
}
