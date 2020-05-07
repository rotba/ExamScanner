package com.example.examscanner.persistence.remote;

import com.example.examscanner.persistence.remote.entities.Exam;
import com.example.examscanner.persistence.remote.entities.Version;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;
import java.util.Observable;

import io.reactivex.Completable;

class RemoteDatabaseFacadeImpl implements RemoteDatabaseFacade {
    @Override
    public Completable createExam(long examId, String courseName, String url, String year, int term, int semester, String mangerId, String[] gradersIdentifiers, long sessionId) {
        return storeObjectInPath(
                String.format("%s/%d", Paths.toExams,examId),
                new Exam(
                        mangerId,
                        Arrays.asList(gradersIdentifiers),
                        courseName,
                        semester,
                        term,
                        year,
                        false
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
    public Completable addVersion(long versionId,long examId, int versionNumber) {
        return storeObjectInPath(
                String.format("%s/%d", Paths.toVersions, versionId),
                new Version(
                        examId,
                        versionNumber
                )
        );
    }

    private static Completable storeObjectInPath(String path, Object obj) {
        DatabaseReference myRef = FirebaseDatabaseFactory.get().getReference(path);
        return Completable.fromCallable(() -> {
            final Task<Void> task = myRef.setValue(obj);
            Tasks.await(task);
            if (task.isSuccessful()) {
                return Completable.complete();
            } else {
                return Completable.error(new IllegalStateException("Task not successful", task.getException()));
            }
        });
    }
}
