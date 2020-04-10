package com.example.examscanner.persistence.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.examscanner.persistence.entities.ExamineeSolution;
import com.example.examscanner.persistence.entities.relations.ExamineeSolutionWithExamineeAnswers;
import com.example.examscanner.persistence.entities.relations.SessionWithExamineeSolutions;

import java.util.List;

@Dao
public interface ExamineeSolutionDao {
    @Insert
    long insert(ExamineeSolution es);

    @Query("SELECT * FROM ExamineeSolution")
    List<ExamineeSolution> getAll();

    @Transaction
    @Query("SELECT * FROM examineesolution")
    List<ExamineeSolutionWithExamineeAnswers> getExamineeSolutionWithExamineeAnswers();

    @Transaction
    @Query("SELECT *  FROM session WHERE id IS :sId LIMIT 1")
    SessionWithExamineeSolutions getSessionWithExamineeSolutions(long sId);
}
