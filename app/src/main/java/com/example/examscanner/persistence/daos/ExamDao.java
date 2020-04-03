package com.example.examscanner.persistence.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.examscanner.persistence.entities.Exam;
import com.example.examscanner.persistence.entities.relations.ExamWithVersions;
import com.example.examscanner.persistence.entities.relations.ExamineeSolutionWithExamineeAnswers;

import java.util.List;

@Dao
public interface ExamDao {
    @Insert
    long insert(Exam exam);

    @Query("SELECT *  FROM exam WHERE id IS :id LIMIT 1")
    Exam getById(long id);

    @Transaction
    @Query("SELECT * FROM exam WHERE id IS :id LIMIT 1")
    ExamWithVersions getExamWithVersions(long id);

    @Query("SELECT * FROM exam WHERE sessionId IS :sId LIMIT 1")
    long getBySessionId(long sId);

    @Query("SELECT *  FROM exam")
    List<Exam> getAll();

    @Update
    void update(Exam exam);
}
