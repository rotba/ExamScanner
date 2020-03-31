package com.example.examscanner.communication;

import android.graphics.Bitmap;

import androidx.room.Room;

import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ExamineeSolutionsEntityInterface;
import com.example.examscanner.communication.entities_interfaces.SemiScannedCaptureEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;
import com.example.examscanner.persistence.AppDatabase;
import com.example.examscanner.persistence.entities.SemiScannedCapture;
import com.example.examscanner.persistence.files_management.FilesManager;
import com.example.examscanner.persistence.files_management.FilesManagerFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

public class RealFacadeImple implements CommunicationFacade {
    private static RealFacadeImple instance;
    private AppDatabase db;
    private FilesManager fm;

    public static RealFacadeImple getInstance() {
        if (instance == null) {
            instance = new RealFacadeImple(
                    Room.databaseBuilder(getApplicationContext(),AppDatabase.class, "database-name").build(),
                    FilesManagerFactory.create()
            );
            return instance;
        } else {
            return instance;
        }
    }

    public RealFacadeImple(AppDatabase db, FilesManager fm) {
        this.db = db;
        this.fm = fm;
    }

    @Override
    public JSONObject getExam(long id) {
        return null;
    }

    @Override
    public JSONObject getVersion(int id) {
        return null;
    }

    @Override
    public JSONArray getExams() {
        return null;
    }

    @Override
    public JSONObject getGrader(int id) {
        return null;
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
    public long getNewSession(String desctiprion) {
        return 0;
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

    @Override
    public List<SemiScannedCaptureEntityInterface> getSemiScannedCaptureBySession(long sId) {
        return null;
    }

    @Override
    public List<ExamineeSolutionsEntityInterface> getExamineeSolutionsBySession(long sId) {
        return null;
    }

    @Override
    public void uploadExamineesSolutions(long[] ids) {

    }

    @Override
    public ExamEntityInterface getExamBySession(long sId) {
        return null;
    }

    @Override
    public VersionEntityInterface getVersionByExamIdAndNumber(long eId, int num) {
        return null;
    }
}
