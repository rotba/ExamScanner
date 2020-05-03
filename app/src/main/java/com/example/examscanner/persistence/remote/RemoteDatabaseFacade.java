package com.example.examscanner.persistence.remote;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface RemoteDatabaseFacade {
    public Observable<String> createExam(String courseName, String url, String year, int term, int semester, String mangerId, String[] gradersIdentifiers , boolean seal, long sessionId);
    public Observable<String> addVersion(long versionId,long examId, int versionNumber);
}
