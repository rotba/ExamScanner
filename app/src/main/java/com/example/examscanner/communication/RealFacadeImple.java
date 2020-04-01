package com.example.examscanner.communication;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.WorkSource;

import androidx.annotation.RequiresApi;
import androidx.room.Room;

import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.communication.entities_interfaces.SemiScannedCaptureEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;
import com.example.examscanner.persistence.AppDatabase;
import com.example.examscanner.persistence.entities.Exam;
import com.example.examscanner.persistence.entities.ExamineeAnswer;
import com.example.examscanner.persistence.entities.ExamineeSolution;
import com.example.examscanner.persistence.entities.Question;
import com.example.examscanner.persistence.entities.SemiScannedCapture;
import com.example.examscanner.persistence.entities.Session;
import com.example.examscanner.persistence.entities.Version;
import com.example.examscanner.persistence.files_management.FilesManager;
import com.example.examscanner.persistence.files_management.FilesManagerFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

public class RealFacadeImple implements CommunicationFacade {
    private static RealFacadeImple instance;
    private AppDatabase db;
    private FilesManager fm;
    private static AppDatabase dbTestInstance;

    public static RealFacadeImple getInstance() {
        if (instance == null) {
            AppDatabase theDB = dbTestInstance==null?
                    Room.databaseBuilder(getApplicationContext(),AppDatabase.class, "database-name").build():
                    dbTestInstance;
            instance = new RealFacadeImple(
                    theDB,
                    FilesManagerFactory.create()
            );
            return instance;
        } else {
            return instance;
        }
    }
    public static void setDBTestInstance(AppDatabase theDBTestInstance){
        dbTestInstance = theDBTestInstance;
    }

    private RealFacadeImple(AppDatabase db, FilesManager fm) {
        this.db = db;
        this.fm = fm;
    }

    @Override
    public JSONObject getExamGoingToEarse(long id) {
        return null;
    }

    @Override
    public JSONObject getVersionGoingToEarse(int id) {
        return null;
    }

    @Override
    public JSONArray getExamsGoingToEarse() {
        return null;
    }

    @Override
    public JSONObject getGraderGoingToEarse(int id) {
        return null;
    }

    @Override
    public long createExam(String courseName, String url, String year, int term, int semester, long sessionId) {
        return db.getExamDao().insert(new Exam(courseName, term, year, url, semester, sessionId));
    }

    @Override
    public long createSemiScannedCapture(int leftMostX, int upperMostY, int rightMostX, int rightMostY, long sessionId, long versionId, Bitmap bm) {
        long bmId = fm.store(bm);
        return db.getSemiScannedCaptureDao().insert(
                new SemiScannedCapture(
                        leftMostX,
                        upperMostY,
                        rightMostX,
                        rightMostY,
                        bmId,
                        versionId,
                        sessionId
                )
        );
    }

    @Override
    public long createNewSession(String desctiprion) {
        return db.getSessionDao().insert(new Session(desctiprion));
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
            public long getVesrionId() {
                return ssc.getVersionId();
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
    public long getExamIdBySession(long sId) {
        return db.getExamDao().getBySessionId(sId);
    }

    @Override
    public ExamEntityInterface getExamById(long id) {
        Exam theExam = db.getExamDao().getById(id);
        return new ExamEntityInterface() {
            @Override
            public long getID() {
                return theExam.getId();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public long[] getVersionsIds() {
                return db.getExamDao().getExamWithVersions(id).getVersions().stream()
                        .mapToLong(Version::getExamId).toArray();
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
                return theExam.getSessionId();
            }

            @Override
            public int getSemester() {
                return theExam.getSemester();
            }
        };
    }

    @Override
    public VersionEntityInterface getVersionByExamIdAndNumber(long eId, int num) {
        Version theVersion = db.getVersionDao().ByExamIdAndNumber(eId, num);
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
    public long createVersion(long examId, int versionNumber) {
        return db.getVersionDao().insert(new Version(versionNumber, examId));
    }

    @Override
    public long addQuestion(long vId, int qNum, int correctAnswer) {
        return db.getQuestionDao().insert(new Question(qNum,vId,correctAnswer));
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
}
