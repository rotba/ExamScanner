package com.example.examscanner.persistence.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.examscanner.persistence.entities.Exam;
import com.example.examscanner.persistence.entities.Session;

import java.util.List;

public class SessionWithExams {
    @Embedded
    private Session session;
    @Relation(
            parentColumn = Session.pkName,
            entityColumn = Exam.fkSid
    )
    private List<Exam> exams;

    public SessionWithExams(Session session, List<Exam> exams) {
        this.session = session;
        this.exams = exams;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }
}
