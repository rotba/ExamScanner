package com.example.examscanner.persistence.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Session {
    public static final String pkName = "id";
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String description;

    public Session(String description) {
        this.description = description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }
}
