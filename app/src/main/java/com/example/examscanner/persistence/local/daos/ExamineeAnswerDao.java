package com.example.examscanner.persistence.local.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.examscanner.persistence.local.entities.ExamineeAnswer;
import com.example.examscanner.persistence.local.entities.relations.ExamineeSolutionWithExamineeAnswers;

@Dao
public interface ExamineeAnswerDao {

    @Insert
    long insert(ExamineeAnswer examineeAnswer);

    @Query("SELECT * FROM EXAMINEESOLUTION WHERE id IS :esId LIMIT 1")
    ExamineeSolutionWithExamineeAnswers getExamineeSolutionWithExamineeAnswers(long esId);
}
