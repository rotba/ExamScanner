package com.example.examscanner.persistence.daos;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.examscanner.persistence.entities.ExamCreationSession;

@Dao
public interface ExamCreationSessionDao {
    @Insert
    long insert(ExamCreationSession examCreationSession);
}
