package com.example.examscanner.persistence.remote;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.examscanner.persistence.remote.entities.Exam;
import com.example.examscanner.persistence.remote.entities.Grader;
import com.example.examscanner.persistence.remote.entities.Question;
import com.example.examscanner.persistence.remote.entities.Version;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;

class RemoteDatabaseFacadeImpl implements RemoteDatabaseFacade {
    @Override
    public Observable<String> createExam(String courseName, String url, String year, int term, int semester, String mangerId, String[] gradersIdentifiers, boolean seal, long sessionId) {
        return storeChildInPath(
                Paths.toExams,
                new Exam(
                        mangerId,
                        Arrays.asList(gradersIdentifiers),
                        courseName,
                        semester,
                        term,
                        year,
                        seal,
                        url
                )
        );
    }

    @Override
    public Observable<String> addVersion(String examId, int versionNumber) {
        return storeChildInPath(
                Paths.toVersions,
                new Version(
                        examId,
                        versionNumber
                )
        );
    }

    @Override
    public Observable<List<Grader>> getGraders() {
        return getChildrenOfRoot(
                Paths.toGraders,
                ds -> {
                    return new Grader(
                            ds.child(Paths.gId).getValue(String.class),
                            ds.getKey() //The grader id is it's user name
                    );
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Observable<List<Exam>> getExams() {
        return getChildrenOfRoot(
                Paths.toExams,
                new DataSnapshot2Obj<Exam>() {
                    DataSnapshotList2ObjList<String> graderListConverter = iterable ->{
                        List<String> ans = new ArrayList<>();
                        iterable.forEach(ds-> ans.add(ds.getKey()));
                        return ans;
                    };
                    @Override
                    public Exam convert(DataSnapshot ds) {
                        return new Exam(
                                ds.child(Exam.metaManager).getValue(String.class),
                                graderListConverter.convert(ds.child(Exam.metaGraders).getChildren()),
                                ds.child(Exam.metaCourseName).getValue(String.class),
                                ds.child(Exam.metaSemester).getValue(Integer.class),
                                ds.child(Exam.metaTerm).getValue(Integer.class),
                                ds.child(Exam.metaYear).getValue(String.class),
                                ds.child(Exam.metaSeal).getValue(Boolean.class),
                                ds.child(Exam.metaUrl).getValue(String.class)
                        );
                    }
                }
        );
    }

    @NotNull
    protected <T> Observable<List<T>> getChildrenOfRoot(String pathToParent, DataSnapshot2Obj<T> ds2o) {
        return new Observable<List<T>>() {
            @Override
            protected void subscribeActual(Observer<? super List<T>> observer) {
                DatabaseReference ref = FirebaseDatabaseFactory.get().getReference(pathToParent);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<T> ans = new ArrayList<>();
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            ans.add(ds2o.convert(childSnapshot));
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
    public Observable<String> createGrader(String userName, String userId) {
        return storeObjectInLocationInPath(
                String.format("%s/%s", Paths.toGraders, userName),
                new Grader(userId, userName)
        );
    }


    @Override
    public Observable<List<Version>> getVersions() {
        return null;
    }

    @Override
    public Observable<List<Question>> getQuestions() {
        return null;
    }

    private static Observable<String> storeChildInPath(String parent, Object obj) {
        return storeObject(() -> FirebaseDatabaseFactory.get().getReference(parent).push(), obj);
    }

    private Observable<String> storeObjectInLocationInPath(String location, Object obj) {
        return storeObject(() -> FirebaseDatabaseFactory.get().getReference(location), obj);
    }

    private static Observable<String> storeObject(ReferenceLocationAllocator allocator, Object obj) {
        return Observable.fromCallable(() -> {
            DatabaseReference myRef = allocator.allocate();
            final Task<Void> task = myRef.setValue(obj);
            Tasks.await(task);
            if (task.isSuccessful()) {
                return myRef.getKey();
            } else {
                throw new IllegalStateException("Task not successful", task.getException());
            }
        });
    }

    private interface ReferenceLocationAllocator {
        DatabaseReference allocate();
    }

    private interface DataSnapshot2Obj<T> {
        T convert(DataSnapshot ds);
    }

    private interface DataSnapshotList2ObjList<T> {
        List<T> convert(Iterable<DataSnapshot> ds);
    }


}
