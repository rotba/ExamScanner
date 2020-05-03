package com.example.examscanner.persistence.remote;

import io.reactivex.Completable;

public interface RemoteDatabaseFacade {
    public Completable createExam(long examId,String courseName, String url, String year, int term, int semester, String mangerId, String[] gradersIdentifiers , long sessionId);
    public Completable addVersion(long versionId,long examId, int versionNumber);
}
