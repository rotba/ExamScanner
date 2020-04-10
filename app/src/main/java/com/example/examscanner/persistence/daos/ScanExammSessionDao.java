package com.example.examscanner.persistence.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.examscanner.persistence.entities.Exam;
import com.example.examscanner.persistence.entities.ScanExamSession;
import com.example.examscanner.persistence.entities.relations.SESessionWithSSCs;

import java.util.List;

@Dao
public interface ScanExammSessionDao {
    @Query("SELECT * FROM ScanExamSession")
    List<ScanExamSession> getAll();

    @Query("SELECT * FROM ScanExamSession WHERE id IS :scanExamSessionId LIMIT 1")
    ScanExamSession getById(long scanExamSessionId);

    @Insert
    long[] insertAll(ScanExamSession... scanExamSessions);

    @Insert
    long insert(ScanExamSession scanExamSession);

    @Delete
    void delete(ScanExamSession scanExamSession);
}
