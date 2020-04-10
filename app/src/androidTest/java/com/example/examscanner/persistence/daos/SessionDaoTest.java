package com.example.examscanner.persistence.daos;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.examscanner.persistence.AppDatabase;
import com.example.examscanner.persistence.entities.Session;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;


public class SessionDaoTest extends DaoAbstractTest{

    private static final String TAG = "SessionDaoTest";

    private SessionDao sessionDao;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        sessionDao = db.getSessionDao();
    }

    @Test
    public void insertAll() {
        sessionDao.insertAll(new Session(TAG),new Session(TAG));
        List<Session> allSessions =sessionDao.getAll();
        Assert.assertTrue(allSessions.size()==2);
    }
}