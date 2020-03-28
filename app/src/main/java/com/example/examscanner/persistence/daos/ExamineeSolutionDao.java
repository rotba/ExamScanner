package com.example.examscanner.persistence.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.examscanner.persistence.entities.ExamineeSolution;
import com.example.examscanner.persistence.entities.relations.ExamineeSolutionWithExamineeAnswers;

import java.util.List;

@Dao
public interface ExamineeSolutionDao {
    @Insert
    long insert(ExamineeSolution es);

    @Transaction
    @Query("SELECT * FROM examineesolution")
    List<ExamineeSolutionWithExamineeAnswers> getExamineeSolutionWithExamineeAnswers();
}
