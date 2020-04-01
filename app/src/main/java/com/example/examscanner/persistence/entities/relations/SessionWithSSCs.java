package com.example.examscanner.persistence.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.examscanner.persistence.entities.SemiScannedCapture;
import com.example.examscanner.persistence.entities.Session;

import java.util.List;

public class SessionWithSSCs {
    @Embedded
    private Session session;

    @Relation(
            parentColumn = Session.pkName,
            entityColumn = SemiScannedCapture.fkSessionId
    )
    private List<SemiScannedCapture> sscs;

    public SessionWithSSCs(Session session, List<SemiScannedCapture> sscs) {
        this.session = session;
        this.sscs = sscs;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<SemiScannedCapture> getSscs() {
        return sscs;
    }

    public void setSscs(List<SemiScannedCapture> sscs) {
        this.sscs = sscs;
    }
}
