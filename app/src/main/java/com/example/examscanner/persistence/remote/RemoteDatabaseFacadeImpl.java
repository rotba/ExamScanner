package com.example.examscanner.persistence.remote;

import com.example.examscanner.persistence.remote.entities.Exam;
import com.example.examscanner.persistence.remote.entities.Grader;
import com.example.examscanner.persistence.remote.entities.Version;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;

class RemoteDatabaseFacadeImpl implements RemoteDatabaseFacade {
    @Override
    public Completable createExam(long examId, String courseName, String url, String year, int term, int semester, String mangerId, String[] gradersIdentifiers, boolean seal,long sessionId) {
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
    public Completable addVersion(long versionId,long examId, int versionNumber) {
        return storeObjectInPath(
                String.format("%s/%d", Paths.toVersions, versionId),
                new Version(
                        examId,
                        versionNumber
                )
        );
    }

    @Override
    public Observable<List<Grader>> getGraders() {
        DatabaseReference ref= FirebaseDatabaseFactory.get().getReference(Paths.toGraders);
        return new Observable<List<Grader>>() {
            @Override
            protected void subscribeActual(Observer<? super List<Grader>> observer) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Grader> ans = new ArrayList<>();
                        for (DataSnapshot graderSnapshot: dataSnapshot.getChildren()) {
                            ans.add(new Grader(
                                    graderSnapshot.child(Paths.gId).getValue().toString(),
                                    graderSnapshot.child(Paths.gUsername).getValue().toString()
                            ));
                        }
                        observer.onNext(ans);
                        observer.onComplete();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        observer.onError(databaseError.toException());
                    }
                });
            }
        };
    }

    @Override
    public Completable createGrader(String userName) {
        return storeObjectInPath(
                String.format("%s/%s", Paths.toGraders, userName),
                new Grader("0",userName)
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
