package com.example.examscanner.persistence.daos;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.examscanner.persistence.entities.Question;

@Dao
public interface QuestionDao {
    @Insert
    long insert(Question question);
}
