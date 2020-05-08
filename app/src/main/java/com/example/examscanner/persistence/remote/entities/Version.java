package com.example.examscanner.persistence.remote.entities;

public class Version {
    public String examId;
    public int versionNumber;

    public Version() {
    }

    public Version(String examId, int versionNumber) {
        this.examId = examId;
        this.versionNumber = versionNumber;
    }

    public String _getId() {
        return null;
    }
}
