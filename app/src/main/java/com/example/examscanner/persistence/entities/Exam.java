package com.example.examscanner.persistence.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.Year;

@Entity
public class Exam {
    public static final String pkName = "id";
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String courseName;
    private int term;
    private String year;
    private int semester;
    public static final String fkSid = "sessionId";
    @ForeignKey(entity = Session.class, parentColumns =Session.pkName , childColumns ="sessionId" )
    private long sessionId;

    public Exam(String courseName, int term, String year, int semester, long sessionId) {
        this.courseName = courseName;
        this.term = term;
        this.year = year;
        this.semester = semester;
        this.sessionId = sessionId;
    }
    public int getId() {
        return id;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setId(int id) {
        this.id = id;
    }
}
