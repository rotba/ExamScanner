package com.example.examscanner.persistence.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.examscanner.persistence.entities.Version;
import com.example.examscanner.persistence.entities.relations.VersionWithQuestions;

import java.util.Arrays;
import java.util.List;

@Dao
public interface VersionDao {
    @Insert
    long insert(Version version);

    @Query("SELECT *  FROM version WHERE examId IS :eId AND verNum==:num LIMIT 1")
    Version getByExamIdAndNumber(long eId, int num);

    @Query("SELECT *  FROM version")
    List<Version> getAll();

    @Transaction
    @Query("SELECT * FROM version WHERE id IS :id LIMIT 1")
    VersionWithQuestions getVersionWithQuestions(long id);

}