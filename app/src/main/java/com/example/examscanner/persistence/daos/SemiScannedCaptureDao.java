package com.example.examscanner.persistence.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.examscanner.persistence.entities.SemiScannedCapture;
import com.example.examscanner.persistence.entities.Session;

import java.util.List;

@Dao
public interface SemiScannedCaptureDao {

    @Query("SELECT *  FROM semiscannedcapture")
    List<SemiScannedCapture> getAll();

    @Query("SELECT *  FROM semiscannedcapture WHERE id IS :id LIMIT 1")
    SemiScannedCapture findById(long id);

    @Insert
    long[] insertAll(SemiScannedCapture... sscs);

    @Insert
    long insert(SemiScannedCapture sscs);
}
