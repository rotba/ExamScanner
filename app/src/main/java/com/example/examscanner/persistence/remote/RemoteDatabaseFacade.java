package com.example.examscanner.persistence.remote;

import com.example.examscanner.communication.entities_interfaces.GraderEntityInterface;
import com.example.examscanner.persistence.remote.entities.Exam;
import com.example.examscanner.persistence.remote.entities.Grader;
import com.example.examscanner.persistence.remote.entities.Question;
import com.example.examscanner.persistence.remote.entities.Version;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface RemoteDatabaseFacade {
    public Completable createExam(long examId,String courseName, String url, String year, int term, int semester, String mangerId, String[] gradersIdentifiers, boolean seal , long sessionId);
    public Completable addVersion(long versionId,long examId, int versionNumber);
    public Observable<List<Grader>> getGraders();
    public Completable createGrader(String userName);
    public Observable<List<Exam>> getExams();
    public Observable<List<Version>> getVersions();
    public Observable<List<Question>> getQuestions();
}
