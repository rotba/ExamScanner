package com.example.examscanner.persistence.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.examscanner.communication.entities_interfaces.QuestionEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;
import com.example.examscanner.persistence.entities.Question;
import com.example.examscanner.persistence.entities.relations.VersionWithQuestions;

@Dao
public interface QuestionDao {
    @Insert
    long insert(Question question);

    @Transaction
    @Query("SELECT * FROM version WHERE id IS :vId LIMIT 1")
    VersionWithQuestions getVersionWithQuestions(long vId);


    @Query("SELECT * FROM question WHERE verId IS :verId AND questionNum IS :qNum LIMIT 1")
    Question getQuestionByVerIdAndQNum(long verId, int qNum);
}
