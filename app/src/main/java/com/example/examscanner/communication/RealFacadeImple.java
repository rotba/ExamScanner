package com.example.examscanner.communication;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ExamineeAnswerEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ExamineeSolutionsEntityInterface;
import com.example.examscanner.communication.entities_interfaces.GraderEntityInterface;
import com.example.examscanner.communication.entities_interfaces.QuestionEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ScanExamSessionEntityInterface;
import com.example.examscanner.communication.entities_interfaces.SemiScannedCaptureEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;
import com.example.examscanner.communication.tasks.Continuation;
import com.example.examscanner.communication.tasks.IdsGenerator;
import com.example.examscanner.communication.tasks.Task;
import com.example.examscanner.communication.tasks.TasksManager;
import com.example.examscanner.communication.tasks.TasksManagerFactory;
import com.example.examscanner.persistence.local.AppDatabase;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.example.examscanner.persistence.local.entities.Exam;
import com.example.examscanner.persistence.local.entities.ExamCreationSession;
import com.example.examscanner.persistence.local.entities.ExamineeAnswer;
import com.example.examscanner.persistence.local.entities.ExamineeSolution;
import com.example.examscanner.persistence.local.entities.Question;
import com.example.examscanner.persistence.local.entities.SemiScannedCapture;
import com.example.examscanner.persistence.local.entities.ScanExamSession;
import com.example.examscanner.persistence.local.entities.Version;
import com.example.examscanner.persistence.local.entities.relations.ExamineeSolutionWithExamineeAnswers;
import com.example.examscanner.persistence.local.files_management.FilesManager;
import com.example.examscanner.persistence.local.files_management.FilesManagerFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacade;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.persistence.remote.entities.Grader;
import com.example.examscanner.persistence.remote.files_management.PathsGenerator;
import com.example.examscanner.persistence.remote.files_management.RemoteFilesManager;
import com.example.examscanner.persistence.remote.files_management.RemoteFilesManagerFactory;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.example.examscanner.persistence.remote.files_management.Utils.toBitmap;
import static com.example.examscanner.persistence.remote.files_management.Utils.toByteArray;


public class RealFacadeImple implements CommunicationFacade {
    private static final long DONT_SAVE_BITMAP = -1;
    private static final int QAD_NUM_OF_QUESTIONS = 50;
    private static RealFacadeImple instance;
    private final String TAG = "ExamScanner";
    private AppDatabase db;
    private FilesManager fm;
    private RemoteFilesManager rfm;
    private RemoteDatabaseFacade remoteDb;
    private TasksManager tasksManager;

    public static synchronized RealFacadeImple getInstance() {
        if (instance == null) {
            instance = new RealFacadeImple(
                    AppDatabaseFactory.getInstance(),
                    FilesManagerFactory.create(),
                    RemoteFilesManagerFactory.get(),
                    RemoteDatabaseFacadeFactory.get(),
                    TasksManagerFactory.get()
            );
            return instance;
        } else {
            return instance;
        }
    }

    static void tearDown() {
        instance = null;
        FilesManagerFactory.tearDown();
    }


    private RealFacadeImple(AppDatabase db, FilesManager fm, RemoteFilesManager rfm, RemoteDatabaseFacade remoteDb, TasksManager tasksManager) {
        this.db = db;
        this.fm = fm;
        this.rfm = rfm;
        this.remoteDb = remoteDb;
        this.tasksManager = tasksManager;
    }


    @Override
    public long createExam(String courseName, String url, String year, int term, int semester, String managerId, String[] graders, long sessionId, int numberOfQuestions, int uploaded) {
        try {
            String remoteId = remoteDb.createExam(courseName, url, year, term, semester, managerId, graders, false, sessionId, numberOfQuestions, uploaded)
                    .blockingFirst();
            long ans = db.getExamDao().insert(new Exam(courseName, term, year, url, semester, sessionId, remoteId, numberOfQuestions, managerId, graders, uploaded));
            Log.d(TAG, String.format("examid %d was created", ans));
            return ans;
        } catch (Throwable e) {
            /*TODO - delete exam*/
            throw new CommunicationException(e);
        }
    }

    @Override
    public long createSemiScannedCapture(int leftMostX, int upperMostY, int rightMostX, int rightMostY, long sessionId, Bitmap bm) {


        final SemiScannedCapture sscs = new SemiScannedCapture(
                leftMostX,
                upperMostY,
                rightMostX,
                rightMostY,
                sessionId
        );
        final long id = db.getSemiScannedCaptureDao().insert(
                sscs
        );
        sscs.setId(id);
        try {
            fm.store(bm, sscs._getBitmapBath());
        } catch (IOException e) {
            throw new CommunicationException(e);
        }
        return id;
    }

    @Override
    public long createNewScanExamSession(long examId) {
        return db.getScanExamSessionDao().insert(new ScanExamSession(examId));
    }

    @Override
    public SemiScannedCaptureEntityInterface getSemiScannedCapture(long id) {
        SemiScannedCapture ssc = db.getSemiScannedCaptureDao().findById(id);
        final Bitmap bitmap;
        try {
            bitmap = fm.get(ssc._getBitmapBath());
        } catch (FileNotFoundException e) {
            throw new CommunicationException("Cannot find bitmap of semi scanned capture");
        }
//        final Bitmap bitmap = null;
        return new SemiScannedCaptureEntityInterface() {
            @Override
            public int getLeftMostX() {
                return ssc.getLeftMostX();
            }

            @Override
            public int getUpperMostY() {
                return ssc.getUpperMostY();
            }

            @Override
            public int getRightMostX() {
                return ssc.getRightMostX();
            }

            @Override
            public int getBottomMosty() {
                return ssc.getBottomMostY();
            }


            @Override
            public long getSessionId() {
                return ssc.getSessionId();
            }

            @Override
            public Bitmap getBitmap() {
                return bitmap;
            }

            @Override
            public long getId() {
                return ssc.getId();
            }
        };
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public long[] getSemiScannedCaptureBySession(long sId) {
        return db.getSemiScannedCaptureDao().getSessionWithSSCs(sId).getSscs().stream()
                .mapToLong(SemiScannedCapture::getId).toArray();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public long[] getExamineeSolutionsBySession(long sId) {
        return db.getExamineeSolutionDao().getSessionWithExamineeSolutions(sId).getExamineeSolutions()
                .stream().mapToLong(ExamineeSolution::getId).toArray();
    }

    @Override
    public long addExamineeAnswer(long solutionId, long questionId, int ans, int leftX, int upY, int rightX, int botY) {
        ExamineeSolution es = db.getExamineeSolutionDao().getById(solutionId);
        Question q = db.getQuestionDao().get(questionId);
        if (es == null) {
            throw new MyAssertionError("ExamineeSolution should exist in db");
        }
        if (q == null) {
            throw new MyAssertionError("Question should exist in db");
        }
        tasksManager.get(IdsGenerator.forSolution(solutionId)).addContinuation(() -> {
                    ExamineeSolution _es = db.getExamineeSolutionDao().getById(solutionId);
                    remoteDb.offlineInsertAnswerIntoExamineeSolution(_es.getRemoteId(), q.getQuestionNum(), ans);
                },
                Continuation.ShouldNotHappenException()
        );
        return db.getExamineeAnswerDao().insert(new ExamineeAnswer(questionId, solutionId, ans, leftX, upY, rightX, botY));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void addExamineeGrade(long solutionId, float grade) {
        tasksManager.get(IdsGenerator.forSolution(solutionId)).addContinuation(() -> {
                    ExamineeSolution es = db.getExamineeSolutionDao().getById(solutionId);
                    throwCommunicationExceptionWhenNull(es, ExamineeSolution.class, String.format("Examinee soultion %d should exit", solutionId));
                    remoteDb.offilneInsertExamineeSolutionGrade(es.getRemoteId(), grade);
                },
                Continuation.ShouldNotHappenException()
        );
//        remoteDb.insertExamineeIDOrReturnNull(e.getRemoteId(), es.getExamineeId())
//                .subscribe(
//                        result -> handleExamineeIdSuccessInsertion(result, es, remoteversionId, answers, grade, e.getRemoteId(), origBitmap),
//                        throwable -> {
//                            throw new CommunicationException(throwable);
//                        }
//                );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleExamineeIdSuccessInsertion(String result, ExamineeSolution es, String remoteversionId, int[][] answers, float grade, String remoetExamId, Bitmap orig) {
        Log.d(TAG, String.format("inserted the eaminieeid %s to the eaxminee ids table. solution local id is %d", result, es.getId()));
        String examineeId = es.getExamineeId();
        if (result == null) {
            es.setExamineeIdOccupied(true);
            byte[] array = new byte[7];
            new Random().nextBytes(array);
            examineeId += "CONFLITCT_" + new String(array, Charset.forName("UTF-8"));
            es.setExamineeId(examineeId);
        }
        final String finalExamineeID = examineeId;
        String bitmapPath = PathsGenerator.genExamineeSolution(remoetExamId, examineeId);
        String origBitmapPath = PathsGenerator.genExamineeSolutionOrig(remoetExamId, examineeId);
        try {
            Log.d(TAG, String.format("started storring the bitmap of es.getId():%d", es.getId()));
            rfm.store(bitmapPath, toByteArray(fm.get(es.getBitmapPath()))).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                    .subscribe(() -> {
                                rfm.store(origBitmapPath, toByteArray(orig)).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                                        .subscribe(
                                                () -> {
                                                    Log.d(TAG, String.format("done storring the bitmap of es.getId():%d", es.getId()));
                                                    handleSolutionBitmapStorageSuccess(bitmapPath, origBitmapPath, es, finalExamineeID, remoteversionId, answers, grade);
                                                },
                                                throwable -> {
                                                    throw new CommunicationException(throwable);
                                                }
                                        );
                            }
                    );
        } catch (FileNotFoundException e) {
            throw new CommunicationException(e);
        }
    }

    private void handleSolutionBitmapStorageSuccess(String bitmapPath, String origBitmapPath, ExamineeSolution es, String finalExamineeID, String remoteversionId, int[][] answers, float grade) {
        rfm.createUrl(bitmapPath).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(
                url -> {
                    rfm.createUrl(origBitmapPath).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(
                            urlOrig -> {
                                Log.d(TAG, String.format("created the url of es.getId():%d", es.getId()));
                                remoteDb.offlineInsertExamineeSolutionTransaction(finalExamineeID, remoteversionId, answers, grade, url, urlOrig, false).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(
                                        s -> {
                                            Log.d(TAG, String.format("done inserting es.getId():%d as %s", es.getId(), s));
                                            es.setRemoteId(s);
                                            db.getExamineeSolutionDao().update(es);
                                        }
                                );
                            },
                            throwable -> {
                                throw new CommunicationException(throwable);
                            }
                    );
                }
        );
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public long[] getExamineeAnswersIdsByExamineeSolutionId(long esId) {
        return db.getExamineeAnswerDao().getExamineeSolutionWithExamineeAnswers(esId).getExamineeAnswers().
                stream().mapToLong(ExamineeAnswer::getId).toArray();
    }

    @Override
    public void uploadExamineesSolutions(long[] ids) {

    }

    @Override
    public long getExamIdByScanExamSession(long scanExamSessionId) {
        ScanExamSession s = db.getScanExamSessionDao().getById(scanExamSessionId);
        return s.getExamIdOfSession();
    }

    @Override
    public ExamEntityInterface getExamById(long id) {
        Exam theExam = db.getExamDao().getById(id);
        throwCommunicationExceptionWhenNull(theExam, Exam.class, String.format("id is %d, db info: %s", id, db.toString()));
        return examEntity2EntityInterface(theExam);
    }

    @Override
    public QuestionEntityInterface getQuestionByExamIdVerNumAndQNum(long examId, int verNum, int qNum) {
        final Version byExamIdAndNumber = db.getVersionDao().getByExamIdAndNumber(examId, verNum);
        throwCommunicationExceptionWhenNull(byExamIdAndNumber, Version.class, String.format("version should exist, vernum:%d , examid:%d", examId, verNum));
        long verId = byExamIdAndNumber.getId();
        Question theQuestion = db.getQuestionDao().getQuestionByVerIdAndQNum(verId, qNum);
        throwCommunicationExceptionWhenNull(theQuestion, Question.class, String.format("question should exist, verid:%d , qnum:%d", verId, qNum));
        return new QuestionEntityInterface() {
            @Override
            public long getId() {
                return theQuestion.getId();
            }

            @Override
            public long getVersionId() {
                return theQuestion.getVerId();
            }

            @Override
            public long getCorrectAnswer() {
                return theQuestion.getCorrectAns();
            }

            @Override
            public int getLeftX() {
                return theQuestion.getLeftX();
            }

            @Override
            public int getUpY() {
                return theQuestion.getUpY();
            }

            @Override
            public int getRightX() {
                return theQuestion.getRightX();
            }

            @Override
            public int getBottomY() {
                return theQuestion.getBorromY();
            }

            @Override
            public int getNum() {
                return theQuestion.getQuestionNum();
            }
        };
    }

    @Override
    public ExamineeAnswerEntityInterface getExamineeAnswerByExamIdVerNumAndQNumAndExamineeId(long examId, int verNum, int qNum, long examineeId) {
        throw new NotImplentedException();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public ExamEntityInterface[] getExams() {
        List<Exam> examEntities = db.getExamDao().getAll();
        List<Exam> onlyRemoteExams = new ArrayList<>();
        List<com.example.examscanner.persistence.remote.entities.Exam> remoteExams = new ArrayList<>();
        remoteDb.getExams().blockingSubscribe(res -> remoteExams.addAll(res));
        for (com.example.examscanner.persistence.remote.entities.Exam re :
                remoteExams) {
            if (!examExists(examEntities, re)) {
                importRemoteExam(re);
                Exam importedExam = db.getExamDao().getByCoursenameSemeseterTermYear(re.courseName, re.semester, re.term, re.year);
                examEntities.add(importedExam);
                onlyRemoteExams.add(importedExam);
            }
            else{
                onlyRemoteExams.add(localExamEntity(examEntities, re));
            }
        }
        List<Exam> toRemove = new ArrayList<>(examEntities);
        toRemove.removeAll(onlyRemoteExams);
        examEntities.removeAll(toRemove);
        ExamEntityInterface[] ans = new ExamEntityInterface[examEntities.size()];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = examEntity2EntityInterface(examEntities.get(i));
        }
        return ans;
    }


//    @Override
//    public ExamEntityInterface[] getExamsofGrader(String userId) {
//        List<Exam> examEntities = db.getExamDao().getAll();
//        List<com.example.examscanner.persistence.remote.entities.Exam> remoteExams = new ArrayList<>();
//        remoteDb.getExamsOfGrader(userId).blockingSubscribe(res -> remoteExams.addAll(res));
//        for (com.example.examscanner.persistence.remote.entities.Exam re :
//                remoteExams) {
//            if (!examExists(examEntities, re)) {
//                importRemoteExam(re);
//                examEntities.add(db.getExamDao().getByCoursenameSemeseterTermYear(re.courseName, re.semester, re.term, re.year));
//            }
//        }
//        ExamEntityInterface[] ans = new ExamEntityInterface[examEntities.size()];
//        for (int i = 0; i < ans.length; i++) {
//            ans[i] = examEntity2EntityInterface(examEntities.get(i));
//        }
//        return ans;
//    }


    private void importRemoteExam(com.example.examscanner.persistence.remote.entities.Exam re) {
        long eId = db.getExamDao().insert(new Exam(re.courseName, re.term, re.year, re.url, re.semester, -1, re._getId(), re.numberOfQuestions, re.manager, re.graders.toArray(new String[0]), re.uploaded));
        List<com.example.examscanner.persistence.remote.entities.Version> remoteVersions = new ArrayList<>();
        remoteDb.getVersions().blockingSubscribe(rvs -> remoteVersions.addAll(rvs));
        for (com.example.examscanner.persistence.remote.entities.Version rv :
                remoteVersions) {
            if (rv.examId.equals(re._getId())) {
                importRemoteVersion(rv, eId);
            }
        }
    }

    private void importRemoteVersion(com.example.examscanner.persistence.remote.entities.Version rv, long examId) {
        final Version version = new Version(rv.versionNumber, examId, rv._getId());
        long vId = db.getVersionDao().insert(version);
        version.setId(vId);
        final Bitmap[] bmBox = {null};
        rfm.get(rv.bitmapPath).blockingSubscribe(bytes -> bmBox[0] = toBitmap(bytes));
        assert bmBox[0] != null;
        Bitmap bm = bmBox[0];
        try {
            fm.store(bm, version._getBitmapPath());
        } catch (IOException e) {
            throw new CommunicationException(e);
        }
        List<com.example.examscanner.persistence.remote.entities.Question> remoteQuestions = new ArrayList<>();
        remoteDb.getQuestions().blockingSubscribe(rqs -> remoteQuestions.addAll(rqs));
        for (com.example.examscanner.persistence.remote.entities.Question q :
                remoteQuestions) {
            if (q.versionId.equals(rv._getId()))
                importRemoteQuestion(q, vId);
        }
    }

    private void importRemoteQuestion(com.example.examscanner.persistence.remote.entities.Question rq, long verionId) {
        db.getQuestionDao().insert(new Question(rq.num, verionId, rq.ans, rq.left, rq.up, rq.right, rq.bottom, rq._getId()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean examExists(List<Exam> examEntities, com.example.examscanner.persistence.remote.entities.Exam re) {
        Object[] arr = examEntities.stream().filter(s ->
                s.getCourseName().equals(re.courseName) &&
                        s.getSemester() == re.semester &&
                        s.getTerm() == re.term
        ).toArray();
        return arr.length == 1;
    }

    private Exam localExamEntity(List<Exam> examEntities, com.example.examscanner.persistence.remote.entities.Exam re) {
        Object[] arr = examEntities.stream().filter(s ->
                s.getCourseName().equals(re.courseName) &&
                        s.getSemester() == re.semester &&
                        s.getTerm() == re.term
        ).toArray();
        return (Exam)arr[0];
    }

    @Override
    public void updateExam(long id, String courseName, int semester, int term, long[] versions, long sessionId, String year) {
        String remoteId;
        try {
            remoteId = db.getExamDao().getById(id).getRemoteId();
        } catch (Throwable e) {
            throw new MyAssertionError("assert updateExam::db.getExamDao().getById(id).getRemoteId()!=null violated", e);
        }
        Exam e = new Exam(courseName, term, year, "THE_EMPTY_URL", semester, sessionId, remoteId, QAD_NUM_OF_QUESTIONS, null, null, 0);
        e.setId(id);
        db.getExamDao().update(e);
    }

    @Override
    public long createNewCreateExamSession() {
        return db.getExamCreationSessionDao().insert(new ExamCreationSession());
    }

    @Override
    public ScanExamSessionEntityInterface[] getScanExamSessions() {
        List<ScanExamSession> entities = db.getScanExamSessionDao().getAll();
        ScanExamSessionEntityInterface[] entitiesInterfaces = new ScanExamSessionEntityInterface[entities.size()];
        for (int i = 0; i < entities.size(); i++) {
            int finalI = i;
            entitiesInterfaces[i] = new ScanExamSessionEntityInterface() {

                @Override
                public long getId() {
                    return entities.get(finalI).getId();
                }

                @Override
                public long getExamId() {
                    return entities.get(finalI).getExamIdOfSession();
                }
            };
        }
        return entitiesInterfaces;
    }

    @Override
    public long createQuestion(long versionId, int num, int ans, int left, int up, int right, int bottom) {

        Version v = db.getVersionDao().getById(versionId);
        if (v == null)
            throw new CommunicationException("Triyng to create a question associated with a version that does not exist");
        try {
            String remoteId = remoteDb.createQuestion(v.getRemoteVersionId(), num, ans, left, up, right, bottom)
                    .blockingFirst();
            return db.getQuestionDao().insert(new Question(num, versionId, ans, left, up, right, bottom, remoteId));
        } catch (Throwable t) {
            /*TODO - delete exam*/
            throw new CommunicationException(t);
        }
    }


    @Override
    public VersionEntityInterface getVersionById(long vId) {
        Version theVersion = db.getVersionDao().getById(vId);
        if (theVersion == null)
            throw new NoSuchElementInLocalDbException(String.format("version with id: %d", vId));
        try {
            return versionEntityToInterface(vId, theVersion);
        } catch (FileNotFoundException e) {
            throw new CommunicationException(e);
        }
    }

    @NotNull
    protected VersionEntityInterface versionEntityToInterface(long vId, Version theVersion) throws FileNotFoundException {
        Bitmap theBitmap = fm.get(theVersion._getBitmapPath());
        return new VersionEntityInterface() {
            @Override
            public long getId() {
                return theVersion.getId();
            }

            @Override
            public long getExamId() {
                return theVersion.getExamId();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public long[] getQuestions() {
                return db.getQuestionDao().getVersionWithQuestions(vId).getQuestions().stream().mapToLong(Question::getId).toArray();
            }

            @Override
            public int getNumber() {
                return theVersion.getVerNum();
            }

            @Override
            public Bitmap getBitmap() {
                return theBitmap;
            }
        };
    }

    @Override
    public QuestionEntityInterface getQuestionById(long qId) {
        Question theQuestion = db.getQuestionDao().get(qId);
        return new QuestionEntityInterface() {
            @Override
            public long getId() {
                return theQuestion.getId();
            }

            @Override
            public long getVersionId() {
                return theQuestion.getVerId();
            }

            @Override
            public long getCorrectAnswer() {
                return theQuestion.getCorrectAns();
            }

            @Override
            public int getLeftX() {
                return theQuestion.getLeftX();
            }

            @Override
            public int getUpY() {
                return theQuestion.getUpY();
            }

            @Override
            public int getRightX() {
                return theQuestion.getRightX();
            }

            @Override
            public int getBottomY() {
                return theQuestion.getBorromY();
            }

            @Override
            public int getNum() {
                return theQuestion.getQuestionNum();
            }
        };
    }

    @Override
    public long insertVersionReplaceOnConflict(long examId, int num, Bitmap perfectImage) {
        Version maybeVersion = db.getVersionDao().getByExamIdAndNumber(examId, num);
        if (maybeVersion == null) {
            final long version = createVersion(examId, num, perfectImage);
            return version;
        } else {
            db.getVersionDao().update(maybeVersion);
            return maybeVersion.getId();
        }
    }

    public void updateUploaded(long examId) {
        String remoteId = db.getExamDao().getExamWithVersions(examId).getExam().getRemoteId();
        remoteDb.updateUploaded(remoteId);
    }

    @Override
    public Observable<String> observeExamineeIds(long id) {
        Exam e = db.getExamDao().getById(id);
        throwCommunicationExceptionWhenNull(e, Exam.class, String.format("id is %d", id));
        return remoteDb.observeExamineeIds(e.getRemoteId());
    }

    @Override
    public void updateExamineeGrade(long id, float grade) {
        ExamineeSolution es = db.getExamineeSolutionDao().getById(id);
        throwCommunicationExceptionWhenNull(es, ExamineeSolution.class, String.format("should exist. id:%s", id));
        remoteDb.offlineUpdateExamineeGrade(es.getRemoteId(), grade);
    }

    @Override
    public void validateSolution(long id) {
        ExamineeSolution es = db.getExamineeSolutionDao().getById(id);
        throwCommunicationExceptionWhenNull(es, ExamineeSolution.class, String.format("should exist. id:%s", id));
        es.setValid(true);
        db.getExamineeSolutionDao().update(es);
    }

    @Override
    public void approveSolution(long id) {
        tasksManager.get(IdsGenerator.forSolution(id)).addContinuation(() -> {
                    ExamineeSolution es = db.getExamineeSolutionDao().getById(id);
                    throwCommunicationExceptionWhenNull(es, ExamineeSolution.class, String.format("should exist. id:%s", id));
                    remoteDb.validateSolution(es.getRemoteId());
                },
                ()->{
                    ExamineeSolution es = db.getExamineeSolutionDao().getById(id);
                    if(es.getRemoteId()==null){
                        throw new CommunicationException("the examinee id should ve stored. nothing to do");
                    }
                }
        );
    }

    private void throwCommunicationExceptionWhenNull(Object o, Class c, String msg) {
        if (o == null) {
            throw new CommunicationException(
                    String.format(
                            "The entity associated with %s ahould not be null\nmsg:%s",
                            c.toString(),
                            msg
                    )
            );
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public long insertQuestionReplaceOnConflict(long vId, int qNum, int qAns, int left, int right, int up, int bottom) {
        Question maybeQuestion = db.getQuestionDao().getQuestionByVerIdAndQNum(vId, qNum);
        if (maybeQuestion == null) {
            return createQuestion(vId, qNum, qAns, left, up, right, bottom);
        } else {
            db.getQuestionDao().update(maybeQuestion);
            return maybeQuestion.getId();
        }
    }

    @Override
    public List<GraderEntityInterface> getGraders() {
        List<GraderEntityInterface> eis = new ArrayList<>();
        List<Grader> graders = new ArrayList<>();
        remoteDb.getGraders().blockingSubscribe(gs -> graders.addAll(gs));
        for (Grader g :
                graders) {
            eis.add(new GraderEntityInterface() {
                @Override
                public String getEmail() {
                    return g.email;
                }

                @Override
                public String getId() {
                    return g.userId;
                }
            });
        }
        return eis;
    }

    @Override
    public void createGrader(String userName, String uesrId) {
        // remoteDb.createGrader(userName, uesrId).blockingFirst();
        remoteDb.addGraderIfAbsent(userName, uesrId);
    }

    @Override
    public ExamineeAnswerEntityInterface getAnswerById(long examineeAnswersId) {
        ExamineeAnswer answer = db.getExamineeAnswerDao().getById(examineeAnswersId);
        Question q = db.getQuestionDao().get(answer.getQuestionId());
        if (q == null) {
            throw new CommunicationException("question should be assosicated with a wuestions");
        }
        if (answer == null) {
            throw new MyAssertionError(String.format("No answer with id:%d", examineeAnswersId));
        }
        if (q == null) {
            throw new MyAssertionError(String.format("No question with id:%d", answer.getQuestionId()));
        }
        return new ExamineeAnswerEntityInterface() {
            @Override
            public long getId() {
                return answer.getId();
            }

            @Override
            public int getAns() {
                return answer.getAns();
            }

            @Override
            public int getNum() {
                return q.getQuestionNum();
            }

            @Override
            public int getLeftX() {
                return answer.getLeftX();
            }

            @Override
            public int getUpY() {
                return answer.getUpY();
            }

            @Override
            public int getRightX() {
                return answer.getRightX();
            }

            @Override
            public int getBottomY() {
                return answer.getBottomY();
            }
        };
    }

    @Override
    public ExamineeSolutionsEntityInterface[] getExamineeSoultions() {
        final List<ExamineeSolution> all = db.getExamineeSolutionDao().getAll();
        ExamineeSolutionsEntityInterface[] ans = new ExamineeSolutionsEntityInterface[all.size()];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = toEntityInterface(all.get(i));
        }
        return ans;
    }

    @Override
    public void removeExamineeSolutionFromCache(long id) {
        ExamineeSolution es = db.getExamineeSolutionDao().getById(id);
        if (es == null) {
            throw new CommunicationException(String.format("ExamineeSolution %d should be in db", id));
        }
        fm.delete(es.getBitmapPath());
        db.getExamineeSolutionDao().delete(es);
    }

    @Override
    public void deleteExamineeSolution(long id) {
        ExamineeSolution es = db.getExamineeSolutionDao().getById(id);
        String remoteId = es.getRemoteId();
        removeExamineeSolutionFromCache(id);
        Version v = db.getVersionDao().getById(es.getVersionId());
        throwCommunicationExceptionWhenNull(v, Version.class, "should exist");
        Exam e = db.getExamDao().getById(v.getExamId());
        throwCommunicationExceptionWhenNull(e, Exam.class, "should exist");
//        es = null;
        remoteDb.offlineDeleteExamineeSolution(remoteId, es.getExamineeId(), e.getRemoteId());
    }

    @Override
    public void updateExamineeAnswer(long solutionId, long questionId, int ans, int leftX, int upY, int rightX, int botY) {
        ExamineeSolution es = db.getExamineeSolutionDao().getById(solutionId);
        if (es == null) {
            throw new CommunicationException("examinee answer should be associated with an existing solution");
        }

        Question q = db.getQuestionDao().get(questionId);
        if (q == null) {
            throw new CommunicationException("question should be associated with an existing solution");
        }
        ExamineeAnswer ea = db.getExamineeAnswerDao().getBySolutionIdAndQuestionId(solutionId, questionId);
        if (ea == null) {
            throw new CommunicationException("examinee answer should be associated with an existing solution");
        }
        ea.setAns(ans);
        ea.setLeftX(leftX);
        ea.setUpY(upY);
        ea.setRightX(rightX);
        ea.setBottomY(botY);
        db.getExamineeAnswerDao().update(ea);
        remoteDb.offlineUpdateAnswerIntoExamineeSolution(es.getRemoteId(), q.getQuestionNum(), ans);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ExamineeSolutionsEntityInterface toEntityInterface(ExamineeSolution examineeSolution) {
//        SemiScannedCapture ssc = db.getSemiScannedCaptureDao().findById(examineeSolution.getScannedCaptureId());
        final List<ExamineeSolutionWithExamineeAnswers> examineeSolutionWithExamineeAnswers = db.getExamineeSolutionDao().getExamineeSolutionWithExamineeAnswers();
        List<ExamineeAnswer> answers = null;
        for (ExamineeSolutionWithExamineeAnswers eWithAs : examineeSolutionWithExamineeAnswers) {
            if (eWithAs.getExamineeSolution().getId() == examineeSolution.getId()) {
                answers = eWithAs.getExamineeAnswers();
            }
        }
        if (answers == null) {
            throw new MyAssertionError("ExamineeSolution should exist in db");
        }
        List<ExamineeAnswer> finalAnswers = answers;
        Bitmap[] bitmap = {null};
        try {
            bitmap[0] = fm.get(examineeSolution.getBitmapPath());
        } catch (FileNotFoundException e) {
            throw new CommunicationException(e);
        }
        return new ExamineeSolutionsEntityInterface() {
            @Override
            public String getExaimneeId() {
                return examineeSolution.getExamineeId();
            }

            @Override
            public long getId() {
                return examineeSolution.getId();
            }

            @Override
            public Bitmap getBitmap() {
                return bitmap[0];//BY DESIGN
            }

            @Override
            public long getSessionId() {
                return examineeSolution.getSessionId();
            }

            @Override
            public long getVersionId() {
                return examineeSolution.getVersionId();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public long[] getExamineeAnswersIds() {
                return finalAnswers.stream().mapToLong(ExamineeAnswer::getId).toArray();
            }

            @Override
            public boolean getExamieeIdIsOccupiedByAnotherSolution() {
                return examineeSolution.isExamineeIdOccupied();
            }

            @Override
            public boolean getIsValid() {
                return examineeSolution.isValid();
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Bitmap createEmprtBitmap() {
        Bitmap ans = Bitmap.createBitmap(2550, 3600, Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(ans);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(ans, 0, 0, null);
        return ans;
    }

    @Override
    public VersionEntityInterface getVersionByExamIdAndNumber(long eId, int num) {
        Version theVersion = db.getVersionDao().getByExamIdAndNumber(eId, num);
        try {
            return versionEntityToInterface(theVersion.getId(), theVersion);
        } catch (FileNotFoundException e) {
            throw new CommunicationException(e);
        }
    }

    @Override
    public long addVersion(long examId, int versionNumber, Bitmap bm) {
        return createVersion(examId, versionNumber, bm);
//        String remoteExamId = db.getExamDao().getById(examId).getRemoteId();
//        try {
//            String remoteVersionId = remoteDb.addVersion(remoteExamId, versionNumber).blockingFirst();
//            long ans = db.getVersionDao().insert(new Version(versionNumber, examId, remoteVersionId));
//            fm.store(bm, generateVersionBitmapPath(ans));//storeBitmap(generateVersionBitmapPath(ans), bm);
//            return ans;
//        } catch (Throwable e) {
//            /*TODO - handle*/
//            throw new CommunicationException(e);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public long createVersion(long examId, int num, Bitmap verBm) {
        Exam e = db.getExamDao().getById(examId);
        if (e == null)
            throw new CommunicationException("Triyng to craete version associated with a version the does not exist");
        try {
            String pathToRemoteBm = PathsGenerator.genVersionPath(e.getRemoteId(), num);
            String remoteId = remoteDb.createVersion(num, e.getRemoteId(), pathToRemoteBm)
                    .blockingFirst();
            final Version version = new Version(num, examId, remoteId);
            final long ans = db.getVersionDao().insert(version);
            version.setId(ans);
            fm.store(verBm, version._getBitmapPath());
            Completable comp = rfm.store(pathToRemoteBm, toByteArray(verBm));
            comp.blockingAwait();
            return ans;
        } catch (Throwable t) {
            /*TODO - delete exam*/
            throw new CommunicationException(t);
        }
    }

    @Override
    public long addQuestion(long vId, int qNum, int correctAnswer, int leftX, int upY, int rightX, int bottomY) {
        return createQuestion(vId, qNum, correctAnswer, leftX, upY, rightX, bottomY);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public long createExamineeSolution(long session, Bitmap bm, String examineeId, long versionId, Bitmap origBm) {
        Version v = db.getVersionDao().getById(versionId);
        throwCommunicationExceptionWhenNull(v, Version.class, String.format("This student solution's version is null, verid:%d", versionId));
        String remoteVersionId = v.getRemoteVersionId();
        final ExamineeSolution es = new ExamineeSolution(examineeId, session, versionId, null, false);
        try {
            final long ans = db.getExamineeSolutionDao().insert(es);
            Log.d(TAG, String.format("inserted %d solution to the local db", ans));
            es.setId(ans);
            tasksManager.post(
                    Completable.fromObservable(remoteDb.onlineInsertExamineeSolution(null, remoteVersionId, false).doOnNext(s -> {
                        ExamineeSolution _es = db.getExamineeSolutionDao().getById(ans);
                        _es.setRemoteId(s);
                        db.getExamineeSolutionDao().update(_es);
                    })),
                    IdsGenerator.forSolution(es.getId()),
                    "Solution uploading"
            );
            onExamineeSolutionUploaded(bm, origBm, examineeId, es.getId());
            fm.store(bm, es.getBitmapPath());
            return ans;
        } catch (IOException e) {
            throw new CommunicationException(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onExamineeSolutionUploaded(Bitmap bm, Bitmap origBm, String examineeId, long id) {
        Task task = tasksManager.get(IdsGenerator.forSolution(id));
        task.addContinuation(() -> tryAcquiringTheExamineeId(examineeId, id), Continuation.ShouldNotHappenException());
        task.addContinuation(() -> uploadExamineeSolutionBitmaps(bm, origBm, id), Continuation.ShouldNotHappenException());
    }

    private void tryAcquiringTheExamineeId(String examineeId, long id) {
        Log.d(TAG, String.format("Acquiring examineeid %s for %d", examineeId, id));
        ExamineeSolution es = db.getExamineeSolutionDao().getById(id);
        throwCommunicationExceptionWhenNull(es, ExamineeSolution.class, String.format("id:%d", id));
        remoteDb.insertExamineeIDOrReturnNull(es.getRemoteId(), examineeId).subscribe(
                result -> {
                    String reservedExamineeId;
                    if (result == null) {
                        es.setExamineeIdOccupied(true);
                        String random = String.valueOf(new Random().nextLong());
                        reservedExamineeId = examineeId + "CONFLITCT_" + random;
                        es.setExamineeId(reservedExamineeId);
                        db.getExamineeSolutionDao().update(es);
                        Log.d(TAG, String.format("Done acquiring examineeid %s for %d", examineeId, id));
                    } else {
                        reservedExamineeId = examineeId;
                    }
                    remoteDb.insertReserevedExamineeId(es.getRemoteId(), reservedExamineeId);
                },
                throwable -> {
                    throw new CommunicationException(throwable);
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadExamineeSolutionBitmaps(Bitmap bm, Bitmap origBm, long id) {
        ExamineeSolution es = db.getExamineeSolutionDao().getById(id);
        throwCommunicationExceptionWhenNull(es, ExamineeSolution.class, String.format("id:%d", id));
        final Version v = db.getVersionDao().getById(es.getVersionId());
        throwCommunicationExceptionWhenNull(v, Version.class, String.format("id:%d", es.getVersionId()));
        Exam e = db.getExamDao().getById(v.getExamId());
        throwCommunicationExceptionWhenNull(e, Exam.class, String.format("id:%d", v.getExamId()));
        Log.d(TAG, String.format("started storring the bitmap of es.getId():%d", es.getId()));
        String bitmapPath = PathsGenerator.genExamineeSolution(e.getRemoteId(), es.getRemoteId());
        String origBitmapPath = PathsGenerator.genExamineeSolutionOrig(e.getRemoteId(), es.getRemoteId());
        rfm.store(bitmapPath, toByteArray(bm)).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(() -> {
                            Log.d(TAG, String.format("done storing the solution bitmap es.getId():%d", es.getId()));
                            rfm.createUrl(bitmapPath).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(
                                    url -> {
                                        remoteDb.setSolutionBitmapUrl(url, es.getRemoteId());
                                        Log.d(TAG, String.format("done creating the solution bitmap url es.getId():%d", es.getId()));
                                    },
                                    throwable -> {
                                        throw new CommunicationException(throwable);
                                    }
                            );
                        },
                        throwable -> {
                            throw new CommunicationException(throwable);
                        }
                );
        rfm.store(origBitmapPath, toByteArray(origBm)).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(
                        () -> {
                            Log.d(TAG, String.format("done storring the orig bitmap of es.getId():%d", es.getId()));
                            rfm.createUrl(origBitmapPath).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(
                                    url -> {
                                        remoteDb.setOriginialBitmapUrl(url, es.getRemoteId());
                                        Log.d(TAG, String.format("done creating the orig bitmap url es.getId():%d", es.getId()));
                                    },
                                    throwable -> {
                                        throw new CommunicationException(throwable);
                                    }
                            );
                        },
                        throwable -> {
                            throw new CommunicationException(throwable);
                        }
                );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public long[] getQuestionsByVersionId(long vId) {
        return db.getQuestionDao().getVersionWithQuestions(vId).getQuestions().
                stream().mapToLong(Question::getId).toArray();
    }

    @NotNull
    private ExamEntityInterface examEntity2EntityInterface(Exam theExam) {
        long id = theExam.getId();
        return new ExamEntityInterface() {
            @Override
            public long getID() {
                return theExam.getId();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public long[] getVersionsIds() {
                return db.getExamDao().getExamWithVersions(id).getVersions().stream()
                        .mapToLong(Version::getId).toArray();
            }

            @Override
            public String getCourseName() {
                return theExam.getCourseName();
            }

            @Override
            public String getUrl() {
                return theExam.getUrl();
            }

            @Override
            public String getYear() {
                return theExam.getYear();
            }

            @Override
            public int getTerm() {
                return theExam.getTerm();
            }

            @Override
            public long getSessionId() {
                return theExam.getExamCreationSessionId();
            }

            @Override
            public int getSemester() {
                return theExam.getSemester();
            }

            @Override
            public int getNumOfQuestions() {
                return theExam.getNumberOfQuestions();
            }

            @Override
            public String getManagerId() {
                return theExam.getManagerId();
            }

            @Override
            public String[] getGradersIds() {
                return theExam.getGradersIds();
            }

            @Override
            public int getUploaded() {
                return theExam.getUploaded();
            }

        };
    }

    private class MyAssertionError extends RuntimeException {
        public MyAssertionError(String s, Throwable e) {
            super(e);
        }

        public MyAssertionError(String s) {
            super(s);
        }
    }

    public class NotImplentedException extends RuntimeException {
    }
}
