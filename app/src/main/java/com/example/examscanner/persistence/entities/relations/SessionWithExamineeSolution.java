package com.example.examscanner.persistence.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.examscanner.persistence.entities.ExamineeSolution;
import com.example.examscanner.persistence.entities.Session;

import java.util.List;

public class SessionWithExamineeSolution {
    @Embedded
    private Session session;
    @Relation(
            parentColumn = Session.pkName,
            entityColumn = ExamineeSolution.fkSession
    )
    private List<ExamineeSolution> examineeSolutions;

    public SessionWithExamineeSolution(Session session, List<ExamineeSolution> examineeSolutions) {
        this.session = session;
        this.examineeSolutions = examineeSolutions;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<ExamineeSolution> getExamineeSolutions() {
        return examineeSolutions;
    }

    public void setExamineeSolutions(List<ExamineeSolution> examineeSolutions) {
        this.examineeSolutions = examineeSolutions;
    }
}
