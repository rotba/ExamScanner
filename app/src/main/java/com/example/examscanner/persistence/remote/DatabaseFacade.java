package com.example.examscanner.persistence.remote;

import io.reactivex.Completable;

public interface DatabaseFacade {
    public Completable createExam(String courseName, String url, String year, int term, int semester, String mangerId, String[] gradersIdentifiers , long sessionId);
}
