package com.example.examscanner.persistence.daos;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.persistence.AppDatabase;
import com.example.examscanner.persistence.entities.Session;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SessionDaoTest {
    private SessionDao sessionDao;
    private AppDatabase db;

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        sessionDao = db.getSessionDao();
    }
    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insertAll() {
        sessionDao.insertAll(new Session(0),new Session(1));
        List<Session> allSessions =sessionDao.getAll();
        Assert.assertTrue(allSessions.size()==2);
        boolean _0IsIn = false;
        boolean _1IsIn = false;
        for (Session s:allSessions) {
            _0IsIn|=s.getId()==0;
            _1IsIn|=s.getId()==1;
        }
        Assert.assertTrue(_0IsIn);
        Assert.assertTrue(_1IsIn);
    }
}