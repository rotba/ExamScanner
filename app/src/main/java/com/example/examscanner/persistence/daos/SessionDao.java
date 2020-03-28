package com.example.examscanner.persistence.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.examscanner.persistence.entities.Session;

import java.util.List;

@Dao
public interface SessionDao {
    @Query("SELECT * FROM session")
    List<Session> getAll();

    @Insert
    void insertAll(Session... sessions);

    @Delete
    void delete(Session session);
}
