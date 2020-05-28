package com.example.examscanner.persistence.remote;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.examscanner.persistence.remote.entities.Exam;
import com.example.examscanner.persistence.remote.entities.ExamineeSolution;
import com.example.examscanner.persistence.remote.entities.Grader;
import com.example.examscanner.persistence.remote.entities.Question;
import com.example.examscanner.persistence.remote.entities.Version;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;

class RemoteDatabaseFacadeImpl implements RemoteDatabaseFacade {

    private static final String TAG = "ExamScanner";
    private static final String MSG_PREF = "RemoteDatabaseFacadeImpl";

    private void storeVersionBitmap(Bitmap verBm) {

    }

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
                ),
                StoreTaskPostprocessor.getOnline()
        );
    }

    @Override
    public Observable<String> createVersion(int num, String remoteExamId,String pathToBitmap) {
        return storeChildInPath(
                Paths.toVersions,
                new Version(
                        remoteExamId,
                        num,
                        pathToBitmap
                ),
                StoreTaskPostprocessor.getOnline()
        );
    }

    @Override
    public Observable<String> createQuestion(String remoteVersionId, int num, int ans, int left, int up, int right, int bottom) {
        return storeChildInPath(
                Paths.toQuestions,
                new Question(
                        num,
                        ans,
                        left,
                        up,
                        right,
                        remoteVersionId,
                        bottom
                ),
                StoreTaskPostprocessor.getOnline()
        );
    }

    @Override
    public Observable<List<ExamineeSolution>> getExamineeSolutions() {
        return getChildrenOfRoot(
                Paths.toSolutions,
                ds -> {
                    List<Long> value = (ArrayList<Long>)ds.child(ExamineeSolution.metaAnswers).getValue();
                    Map<String, Integer> answers = new HashMap<>();
                    for(int i =1; i< value.size(); i++){
                        answers.put(String.valueOf(i) , value.get(i).intValue());
                    }
                    ExamineeSolution examineeSolution = new ExamineeSolution(
                            ds.child(ExamineeSolution.metaVersionId).getValue(Integer.class),
                            answers
                    );
                    examineeSolution._setId(ds.getKey());
                    return examineeSolution;
                }
        );
    }

    @Override
    public void offlineInsertExamineeSolution(String examineeId, long versionId) {
        storeObjectInLocationInPath(
                String.format("%s/%s",Paths.toSolutions,examineeId),
                new ExamineeSolution(
                        versionId,
                        new HashMap<>()
                ),
                StoreTaskPostprocessor.getOffline()
        ).subscribe();
    }

    @Override
    public void offlineInsertAnswerIntoExamineeSolution(String examineeId, int questionNum, int ans) {
        storeObjectInLocationInPath(
                String.format("%s/%s/%s/%d", Paths.toSolutions,examineeId,ExamineeSolution.metaAnswers ,questionNum),
                new Integer(ans),
                StoreTaskPostprocessor.getOffline()
        ).subscribe();
    }

    @Override
    public Observable<String> addVersion(int num, String remoteExamId,String pathToBitmap) {
        return createVersion(num,remoteExamId,pathToBitmap);
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
                        Exam exam = new Exam(
                                ds.child(Exam.metaManager).getValue(String.class),
                                graderListConverter.convert(ds.child(Exam.metaGraders).getChildren()),
                                ds.child(Exam.metaCourseName).getValue(String.class),
                                ds.child(Exam.metaSemester).getValue(Integer.class),
                                ds.child(Exam.metaTerm).getValue(Integer.class),
                                ds.child(Exam.metaYear).getValue(String.class),
                                ds.child(Exam.metaSeal).getValue(Boolean.class),
                                ds.child(Exam.metaUrl).getValue(String.class)
                        );
                        exam._setId(ds.getKey());
                        return exam;
                    }
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Observable<List<Version>> getVersions() {
        return getChildrenOfRoot(
                Paths.toVersions,
                new DataSnapshot2Obj<Version>() {
                    @Override
                    public Version convert(DataSnapshot ds) {
                        Version version = new Version(
                                ds.child(Version.metaExamId).getValue(String.class),
                                ds.child(Version.metaVersionNumber).getValue(Integer.class),
                                ds.child(Version.metaBitmapPath).getValue(String.class)
                        );
                        version._getId(ds.getKey());
                        return version;
                    }
                }
        );
    }

    @Override
    public Observable<List<Question>> getQuestions() {
        return getChildrenOfRoot(
                Paths.toQuestions,
                new DataSnapshot2Obj<Question>() {
                    @Override
                    public Question convert(DataSnapshot ds) {
                        Question question = new Question(
                                ds.child(Question.metaNum).getValue(Integer.class),
                                ds.child(Question.metaAns).getValue(Integer.class),
                                ds.child(Question.metaLeft).getValue(Integer.class),
                                ds.child(Question.metaUp).getValue(Integer.class),
                                ds.child(Question.metaRight).getValue(Integer.class),
                                ds.child(Question.metaVersionId).getValue(String.class),
                                ds.child(Question.metaBottom).getValue(Integer.class)
                        );
                        question._setId(ds.getKey());
                        return question;
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
                new Grader(userId, userName),
                StoreTaskPostprocessor.getOnline()
        );
    }


    private static Observable<String> storeChildInPath(String parent, Object obj, StoreTaskPostprocessor taskPostprocessor) {
        return storeObject(() -> FirebaseDatabaseFactory.get().getReference(parent).push(), obj,taskPostprocessor);
    }

    private Observable<String> storeObjectInLocationInPath(String location, Object obj, StoreTaskPostprocessor taskPostprocessor) {
        return storeObject(() -> FirebaseDatabaseFactory.get().getReference(location), obj,taskPostprocessor);
    }

    private static Observable<String> storeObject(ReferenceLocationAllocator allocator, Object obj, StoreTaskPostprocessor postProcessor) {
        DatabaseReference myRef = allocator.allocate();
        final Task<Void> task = myRef.setValue(obj);
        return postProcessor.postProcess(task, myRef);
//        return Observable.fromCallable(() -> {
//            Tasks.await(task);
//            if (task.isSuccessful()) {
//                return myRef.getKey();
//            } else {
//                throw new IllegalStateException("Task not successful", task.getException());
//            }
//        });
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

    private interface StoreTaskPostprocessor{
        Observable<String> postProcess(Task t, DatabaseReference ref);
        static StoreTaskPostprocessor getOnline(){
            return new StoreTaskPostprocessor() {
                @Override
                public Observable<String> postProcess(Task t, DatabaseReference ref) {
                    return Observable.fromCallable(() -> {
                        Tasks.await(t);
                        if (t.isSuccessful()) {
                            return ref.getKey();
                        } else {
                            throw new IllegalStateException("Task not successful", t.getException());
                        }
                    });
                }
            };
        }

        static StoreTaskPostprocessor getOffline(){
            return new StoreTaskPostprocessor() {
                @Override
                public Observable<String> postProcess(Task t, DatabaseReference ref) {
                    return Observable.fromCallable(() -> {
                        t.addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, MSG_PREF+"::offlineStoreObject SUCCESS");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, MSG_PREF+"::offlineStoreObject FAILURE", e);
                            }
                        });
                        return "return";
                    });
                }
            };
        }
    }
}
