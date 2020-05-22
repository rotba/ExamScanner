package com.example.examscanner.communication;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ExamineeAnswerEntityInterface;
import com.example.examscanner.communication.entities_interfaces.GraderEntityInterface;
import com.example.examscanner.communication.entities_interfaces.QuestionEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ScanExamSessionEntityInterface;
import com.example.examscanner.communication.entities_interfaces.SemiScannedCaptureEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;
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
import com.example.examscanner.persistence.local.files_management.FilesManager;
import com.example.examscanner.persistence.local.files_management.FilesManagerFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacade;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.persistence.remote.entities.Grader;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RealFacadeImple implements CommunicationFacade {
    private static final long DONT_SAVE_BITMAP = -1;
    private static final int QAD_NUM_OF_QUESTIONS = 50;
    private static RealFacadeImple instance;
    private AppDatabase db;
    private FilesManager fm;
    private RemoteDatabaseFacade remoteDb;

    public static synchronized RealFacadeImple getInstance() {
        if (instance == null) {
            instance = new RealFacadeImple(
                    AppDatabaseFactory.getInstance(),
                    FilesManagerFactory.create(),
                    RemoteDatabaseFacadeFactory.get()
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


    private RealFacadeImple(AppDatabase db, FilesManager fm, RemoteDatabaseFacade remoteDb) {
        this.db = db;
        this.fm = fm;
        this.remoteDb = remoteDb;
    }


    @Override
    public long createExam(String courseName, String url, String year, int term, int semester, String managerId, String[] graders, long sessionId, int numberOfQuestions) {
        try {
            String remoteId = remoteDb.createExam(courseName, url, year, term, semester, managerId, graders, false, sessionId)
                    .blockingFirst();
            long ans = db.getExamDao().insert(new Exam(courseName, term, year, url, semester, sessionId, remoteId, numberOfQuestions));
            return ans;
        } catch (Throwable e) {
            /*TODO - delete exam*/
            throw new CommunicationException(e);
        }
    }

    @Override
    public long createSemiScannedCapture(int leftMostX, int upperMostY, int rightMostX, int rightMostY, long sessionId, Bitmap bm) {


        final long id = db.getSemiScannedCaptureDao().insert(
                new SemiScannedCapture(
                        leftMostX,
                        upperMostY,
                        rightMostX,
                        rightMostY,
                        sessionId
                )
        );
        try {
            fm.store(bm, generateSSCBitmapPath(id));
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
            bitmap = fm.get(generateSSCBitmapPath(ssc.getId()));
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
        return db.getExamineeAnswerDao().insert(new ExamineeAnswer(questionId, solutionId, ans, leftX, upY, rightX, botY));
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
        return examEntity2EntityInterface(theExam);
    }

    @Override
    public QuestionEntityInterface getQuestionByExamIdVerNumAndQNum(long examId, int verNum, int qNum) {
        long verId = db.getVersionDao().getByExamIdAndNumber(examId, verNum).getId();
        Question theQuestion = db.getQuestionDao().getQuestionByVerIdAndQNum(verId, qNum);
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
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public ExamEntityInterface[] getExams() {
        List<Exam> examEntities = db.getExamDao().getAll();
        List<com.example.examscanner.persistence.remote.entities.Exam> remoteExams = new ArrayList<>();
        remoteDb.getExams().blockingSubscribe(res -> remoteExams.addAll(res));
        for (com.example.examscanner.persistence.remote.entities.Exam re :
                remoteExams) {
            if (!examExists(examEntities, re)) {
                importRemoteExam(re);
                examEntities.add(db.getExamDao().getByCoursenameSemeseterTermYear(re.courseName, re.semester, re.term, re.year));
            }
        }
        ExamEntityInterface[] ans = new ExamEntityInterface[examEntities.size()];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = examEntity2EntityInterface(examEntities.get(i));
        }
        return ans;
    }


    private void importRemoteExam(com.example.examscanner.persistence.remote.entities.Exam re) {
        long eId = db.getExamDao().insert(new Exam(re.courseName, re.term, re.year, re.url, re.semester, -1, re._getId(), QAD_NUM_OF_QUESTIONS));
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
        long vId = db.getVersionDao().insert(new Version(rv.versionNumber, examId, rv._getId()));
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

    @Override
    public void updateExam(long id, String courseName, int semester, int term, long[] versions, long sessionId, String year) {
        String remoteId;
        try {
            remoteId = db.getExamDao().getById(id).getRemoteId();
        } catch (Throwable e) {
            throw new MyAssertionError("assert updateExam::db.getExamDao().getById(id).getRemoteId()!=null violated", e);
        }
        Exam e = new Exam(courseName, term, year, "THE_EMPTY_URL", semester, sessionId, remoteId, QAD_NUM_OF_QUESTIONS);
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
    public long createVersion(long examId, int num, Bitmap verBm) {
        Exam e = db.getExamDao().getById(examId);
        if (e == null)
            throw new CommunicationException("Triyng to craete version associated with a version the does not exist");
        try {
            String remoteId = remoteDb.createVersion(num, e.getRemoteId(), verBm)
                    .blockingFirst();
            return db.getVersionDao().insert(new Version(num, examId, remoteId));
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
        Bitmap theBitmap = fm.get(generateVersionBitmapPath(theVersion.getId()));
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
            try {
                fm.store(perfectImage, generateVersionBitmapPath(version));
                return version;
            } catch (IOException e) {
                throw new CommunicationException(e);
            }
        } else {
            db.getVersionDao().update(maybeVersion);
            return maybeVersion.getId();
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public long insertQuestionReplaceOnConflict(long vId, int qNum, int qAns, int left, int right, int up, int bottom) {
        Question maybeQuestion = db.getQuestionDao().getQuestionByVerIdAndQNum(vId, qNum);
        if (maybeQuestion == null) {
            return createQuestion(vId, qNum, qAns, left, up, right , bottom);
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
                public String getUserName() {
                    return g._getUserName();
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
        remoteDb.createGrader(userName, uesrId).blockingFirst();
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
        String remoteExamId = db.getExamDao().getById(examId).getRemoteId();
        try {
            String remoteVersionId = remoteDb.addVersion(remoteExamId, versionNumber).blockingFirst();
            long ans = db.getVersionDao().insert(new Version(versionNumber, examId, remoteVersionId));
            fm.store(bm, generateVersionBitmapPath(ans));//storeBitmap(generateVersionBitmapPath(ans), bm);
            return ans;
        } catch (Throwable e) {
            /*TODO - handle*/
            throw new CommunicationException(e);
        }
    }


    private static String generateVersionBitmapPath(long ans) {
        return "VERSION_" + String.valueOf(ans);
    }

    private String generateSSCBitmapPath(long sscId) {
        return "SSC_" + String.valueOf(sscId);
    }

    @Override
    public long addQuestion(long vId, int qNum, int correctAnswer, int leftX, int upY, int rightX, int bottomY) {
        return createQuestion(vId, qNum, correctAnswer, leftX, upY, rightX, bottomY);
    }

    @Override
    public long createExamineeSolution(long sId, Bitmap bm, long examineeId) {
        return db.getExamineeSolutionDao().insert(new ExamineeSolution(examineeId, DONT_SAVE_BITMAP, sId));
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
        };
    }

    private class MyAssertionError extends RuntimeException {
        public MyAssertionError(String s, Throwable e) {
            super(e);
        }
    }
}
