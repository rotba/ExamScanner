package com.example.examscanner.persistence.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Session {
    @PrimaryKey
    private int id;

    public Session(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
