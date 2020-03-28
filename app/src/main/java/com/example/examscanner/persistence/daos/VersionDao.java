package com.example.examscanner.persistence.daos;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.examscanner.persistence.entities.Version;

@Dao
public interface VersionDao {
    @Insert
    long insert(Version version);
}
