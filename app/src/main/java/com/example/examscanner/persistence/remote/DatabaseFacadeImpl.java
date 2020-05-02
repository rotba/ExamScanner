package com.example.examscanner.persistence.remote;

import com.example.examscanner.persistence.remote.entities.Exam;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import io.reactivex.Completable;

class DatabaseFacadeImpl implements DatabaseFacade {
    @Override
    public Completable createExam(String courseName, String url, String year, int term, int semester, String mangerId, String[] gradersIdentifiers, long sessionId) {
        DatabaseReference myRef = FirebaseDatabaseFactory.get().getReference(Paths.toExams);
        return Completable.fromCallable(() -> {
            final Task<Void> task = myRef.setValue(
                    new Exam(
                            mangerId,
                            Arrays.asList(gradersIdentifiers),
                            courseName,
                            semester,
                            term,
                            year
                    )
            );
            Tasks.await(task);
            if (task.isSuccessful()) {
                return Completable.complete();
            } else {
                return Completable.error(new IllegalStateException("Task not successful", task.getException()));
            }
        });
    }
}
