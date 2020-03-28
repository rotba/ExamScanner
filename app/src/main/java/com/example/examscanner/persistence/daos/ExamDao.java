package com.example.examscanner.persistence.daos;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.examscanner.persistence.entities.Exam;

@Dao
public interface ExamDao {
    @Insert
    long insert(Exam exam);
}
