package com.example.examscanner.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.examscanner.persistence.daos.SessionDao;
import com.example.examscanner.persistence.entities.Session;

@Database(entities = {Session.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SessionDao getSessionDao();
}
