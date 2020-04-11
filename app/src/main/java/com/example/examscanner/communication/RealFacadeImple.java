package com.example.examscanner.communication;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ExamineeAnswerEntityInterface;
import com.example.examscanner.communication.entities_interfaces.QuestionEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ScanExamSessionEntityInterface;
import com.example.examscanner.communication.entities_interfaces.SemiScannedCaptureEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;
import com.example.examscanner.persistence.AppDatabase;
import com.example.examscanner.persistence.AppDatabaseFactory;
import com.example.examscanner.persistence.entities.Exam;
import com.example.examscanner.persistence.entities.ExamCreationSession;
import com.example.examscanner.persistence.entities.ExamineeAnswer;
import com.example.examscanner.persistence.entities.ExamineeSolution;
import com.example.examscanner.persistence.entities.Question;
import com.example.examscanner.persistence.entities.SemiScannedCapture;
import com.example.examscanner.persistence.entities.ScanExamSession;
import com.example.examscanner.persistence.entities.Version;
import com.example.examscanner.persistence.files_management.FilesManager;
import com.example.examscanner.persistence.files_management.FilesManagerFactory;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RealFacadeImple implements CommunicationFacade {
    private static RealFacadeImple instance;
    private AppDatabase db;
    private FilesManager fm;

    public static synchronized RealFacadeImple getInstance() {
        if (instance == null) {
            instance = new RealFacadeImple(
                    AppDatabaseFactory.getInstance(),
                    FilesManagerFactory.create()
            );
            return instance;
        } else {
            return instance;
        }
    }
//    static void setTestInstance(AppDatabase theDBTestInstance){
//        theDBTestInstance.clearAllTables();
//        testInstance = new RealFacadeImple(
//                theDBTestInstance,
//                FilesManagerFactory.create()
//        );
//    }
//    static void tearDownTestInstance(){
//        testInstance = null;
//    }

    private RealFacadeImple(AppDatabase db, FilesManager fm) {
        this.db = db;
        this.fm = fm;
    }

    

    @Override
    public long createExam(String courseName, String url, String year, int term, int semester, long sessionId) {
        return db.getExamDao().insert(new Exam(courseName, term, year, url, semester, sessionId));
    }

    @Override
    public long createSemiScannedCapture(int leftMostX, int upperMostY, int rightMostX, int rightMostY, long sessionId,  Bitmap bm) {
        long bmId = fm.store(bm);
        return db.getSemiScannedCaptureDao().insert(
                new SemiScannedCapture(
                        leftMostX,
                        upperMostY,
                        rightMostX,
                        rightMostY,
                        bmId,
                        sessionId
                )
        );
    }

    @Override
    public long createNewScanExamSession(long examId) {
        return db.getScanExamSessionDao().insert(new ScanExamSession(examId));
    }

    @Override
    public SemiScannedCaptureEntityInterface getSemiScannedCapture(long id) {
        SemiScannedCapture ssc = db.getSemiScannedCaptureDao().findById(id);
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
                return fm.get(ssc.getBitmapId());
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
        return db.getExamineeAnswerDao().insert(new ExamineeAnswer(questionId,solutionId,ans,leftX, upY, rightX,  botY));
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
        ScanExamSession s =  db.getScanExamSessionDao().getById(scanExamSessionId);
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
        Question theQuestion = db.getQuestionDao().getQuestionByVerIdAndQNum(verId,qNum);
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

    @Override
    public ExamEntityInterface[] getExams() {
        List<Exam> examEntities = db.getExamDao().getAll();
        ExamEntityInterface[] ans = new ExamEntityInterface[examEntities.size()];
        for (int i = 0; i <ans.length ; i++) {
            ans[i] = examEntity2EntityInterface(examEntities.get(i));
        }
        return ans;
    }

    @Override
    public void updateExam(long id, String courseName, int semester, int term, long[] versions, long sessionId, String year) {
        Exam e = new Exam(courseName,term,year,"THE_EMPTY_URL",semester,sessionId);
        e.setId(id);
        db.getExamDao().update(e);
    }

    @Override
    public long createNewCreateExamSession() {
        return db.getExamCreationSessionDao().insert(new ExamCreationSession());
    }

    @Override
    public ScanExamSessionEntityInterface[] getScanExamSessions() {
        List<ScanExamSession> entities  = db.getScanExamSessionDao().getAll();
        ScanExamSessionEntityInterface[] entitiesInterfaces = new ScanExamSessionEntityInterface[entities.size()];
        for (int i = 0; i <entities.size() ; i++) {
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
        return db.getQuestionDao().insert(new Question(num,versionId,ans,left,up,right,bottom));
    }

    @Override
    public long createVersion(long examId, int num) {
        return db.getVersionDao().insert(new Version(num,examId));
    }

    @Override
    public VersionEntityInterface getVersionById(long vId) {
        Version theVersion =db.getVersionDao().getById(vId);
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
        };
    }

    @Override
    public QuestionEntityInterface getQuestionById(long qId) {
        Question theQuestion =  db.getQuestionDao().get(qId);
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
    public long insertVersionReplaceOnConflict(long examId, int num) {
        Version maybeVersion = db.getVersionDao().getByExamIdAndNumber(examId, num);
        if(maybeVersion==null){
            return db.getVersionDao().insert(new Version(num,examId));
        }else{
            db.getVersionDao().update(maybeVersion);
            return maybeVersion.getId();
        }
    }

    @Override
    public VersionEntityInterface getVersionByExamIdAndNumber(long eId, int num) {
        Version theVersion = db.getVersionDao().getByExamIdAndNumber(eId, num);
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
                return db.getVersionDao().getVersionWithQuestions(theVersion.getId()).getQuestions().stream()
                        .mapToLong(Question::getId).toArray();
            }

            @Override
            public int getNumber() {
                return theVersion.getVerNum();
            }
        };
    }

    @Override
    public long addVersion(long examId, int versionNumber) {
        return db.getVersionDao().insert(new Version(versionNumber, examId));
    }

    @Override
    public long addQuestion(long vId, int qNum, int correctAnswer, int leftX, int upY, int rightX, int bottomY) {
        return db.getQuestionDao().insert(new Question(qNum,vId,correctAnswer,leftX, upY, rightX, bottomY));
    }

    @Override
    public long createExamineeSolution(long sId, Bitmap bm, long examineeId) {
        return db.getExamineeSolutionDao().insert(new ExamineeSolution(examineeId,fm.store(bm),sId));
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
        };
    }
}
