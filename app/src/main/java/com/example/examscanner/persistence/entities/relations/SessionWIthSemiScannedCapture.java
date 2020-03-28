package com.example.examscanner.persistence.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.examscanner.persistence.entities.SemiScannedCapture;
import com.example.examscanner.persistence.entities.Session;

import java.util.List;

public class SessionWIthSemiScannedCapture {
    @Embedded
    private Session session;
    @Relation(
            parentColumn = Session.pkName,
            entityColumn =  SemiScannedCapture.fkSessionId
    )
    private List<SemiScannedCapture> semiScannedCaptures;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<SemiScannedCapture> getSemiScannedCaptures() {
        return semiScannedCaptures;
    }

    public void setSemiScannedCaptures(List<SemiScannedCapture> semiScannedCaptures) {
        this.semiScannedCaptures = semiScannedCaptures;
    }

    public SessionWIthSemiScannedCapture(Session session, List<SemiScannedCapture> semiScannedCaptures) {
        this.session = session;
        this.semiScannedCaptures = semiScannedCaptures;
    }
}
