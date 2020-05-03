package com.example.examscanner.persistence.remote;

import com.example.examscanner.persistence.remote.entities.Exam;
import com.example.examscanner.persistence.remote.entities.Version;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;


import io.reactivex.Completable;
import io.reactivex.Observable;

class RemoteDatabaseFacadeImpl implements RemoteDatabaseFacade {
    @Override
    public Observable<String> createExam(long examId, String courseName, String url, String year, int term, int semester, String mangerId, String[] gradersIdentifiers,boolean seal, long sessionId) {
        return storeObjectInPath(
                String.format("%s/%d", Paths.toExams,examId),
                new Exam(
                        mangerId,
                        Arrays.asList(gradersIdentifiers),
                        courseName,
                        semester,
                        term,
                        year,
                        seal
                )
        );
//        DatabaseReference myRef = FirebaseDatabaseFactory.get().getReference(String.format("%s/%l", Paths.toExams, examId));
//        return Completable.fromCallable(() -> {
//            final Task<Void> task = myRef.setValue(
//                    new Exam(
//                            mangerId,
//                            Arrays.asList(gradersIdentifiers),
//                            courseName,
//                            semester,
//                            term,
//                            year
//                    )
//            );
//            Tasks.await(task);
//            if (task.isSuccessful()) {
//                return Completable.complete();
//            } else {
//                return Completable.error(new IllegalStateException("Task not successful", task.getException()));
//            }
//        });
    }

    @Override
    public Observable<String> addVersion(long versionId, long examId, int versionNumber) {
        return storeObjectInPath(
                String.format("%s/%l", Paths.toVersions, versionId),
                new Version(
                        examId,
                        versionNumber
                )
        );
    }

    private static Observable<String> storeObjectInPath(String path, Object obj) {
        DatabaseReference myRef = FirebaseDatabaseFactory.get().getReference(path);
        return Observable.fromCallable(() -> {
            final Task<Void> task = myRef.push().setValue((Exam)obj);
            Tasks.await(task);
            if (task.isSuccessful()) {
                return myRef.getKey();
            } else {
                throw task.getException();
            }
        });
    }
}
