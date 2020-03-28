package com.example.examscanner.persistence.daos;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.examscanner.persistence.entities.ExamineeAnswer;

@Dao
public interface ExamineeAnswerDao {

    @Insert
    void insert(ExamineeAnswer examineeAnswer);
}
