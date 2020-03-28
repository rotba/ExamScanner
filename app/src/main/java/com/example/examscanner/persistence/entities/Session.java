package com.example.examscanner.persistence.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Session {
    public static final String pkName = "id";
    @PrimaryKey(autoGenerate = true)
    private int id;

    public Session(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public int getId() {
        return id;
    }
}
