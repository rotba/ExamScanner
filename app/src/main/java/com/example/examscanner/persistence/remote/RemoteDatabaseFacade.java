package com.example.examscanner.persistence.remote;

import android.graphics.Bitmap;

import com.example.examscanner.communication.entities_interfaces.GraderEntityInterface;
import com.example.examscanner.persistence.remote.entities.Exam;
import com.example.examscanner.persistence.remote.entities.ExamineeSolution;
import com.example.examscanner.persistence.remote.entities.Grader;
import com.example.examscanner.persistence.remote.entities.Question;
import com.example.examscanner.persistence.remote.entities.Version;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface RemoteDatabaseFacade {
    public Observable<String> createExam(String courseName, String url, String year, int term, int semester, String mangerId, String[] gradersIdentifiers, boolean seal , long sessionId, int numberOfQuestions, int uploaded);
    public Observable<String> addVersion(int num, String remoteExamId, String bitmapPath);
    public Observable<List<Grader>> getGraders();
    public Observable<List<Exam>> getExams();
    public Observable<List<Version>> getVersions();
    public Observable<List<Question>> getQuestions();
    public Observable<String> createGrader(String email, String userId);
    public Observable<String> createVersion(int num, String remoteExamId, String bitmapPath);
    public Observable<String> createQuestion(String remoteVersionId, int num, int ans, int left, int up, int right, int bottom);
    public Observable<List<ExamineeSolution>> getExamineeSolutions();
    public void offlineInsertExamineeSolution(String examineeId, String versionId);
    public void offlineInsertAnswerIntoExamineeSolution(String examineeId, int questionNum, int ans);
    public void offlineDeleteExamineeSolution(String remoteId);
    public void offlineInsertGradeIntoExamineeSolution(String examineeId, float grade);
    public void offlineInsertExamineeSolutionTransaction(String examineeId, String versionId, int[][] answers, float grade);
    public void addGraderIfAbsent(String email, String uId) ;
    public Observable<List<Exam>> getExamsOfGrader(String userId);
    public void offlineUpdateAnswerIntoExamineeSolution(String examineeId, int questionNum, int ans);
    public void updateUploaded(String remoteId);
}
