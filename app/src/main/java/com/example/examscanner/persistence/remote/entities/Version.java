package com.example.examscanner.persistence.remote.entities;

public class Version {
    public long examId;
    public int versionNumber;

    public Version() {
    }

    public Version(long examId, int versionNumber) {
        this.examId = examId;
        this.versionNumber = versionNumber;
    }
}
