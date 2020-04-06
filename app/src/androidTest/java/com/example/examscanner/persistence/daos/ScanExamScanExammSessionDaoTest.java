package com.example.examscanner.persistence.daos;

import com.example.examscanner.persistence.entities.ScanExamSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;


public class ScanExamScanExammSessionDaoTest extends DaoAbstractTest{

    private static final String TAG = "SessionDaoTest";

    private ScanExammSessionDao scanExammSessionDao;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        scanExammSessionDao = db.getScanExamSessionDao();
    }

    @Test
    @Ignore("Stupid test. Need to delete")
    public void insertAll() {
//        scanExammSessionDao.insertAll(new ScanExamSession(TAG),new ScanExamSession(TAG));
//        List<ScanExamSession> allScanExamSessions = scanExammSessionDao.getAll();
//        Assert.assertTrue(allScanExamSessions.size()==2);
    }
}