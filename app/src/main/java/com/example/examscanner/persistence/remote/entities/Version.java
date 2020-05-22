package com.example.examscanner.persistence.remote.entities;

public class Version {
    public static String metaExamId = "examId";
    public static String metaVersionNumber = "versionNumber";
    public String examId;
    public int versionNumber;
    private String id;

    public Version() {
    }

    public Version(String examId, int versionNumber) {
        this.examId = examId;
        this.versionNumber = versionNumber;
    }

    public String _getId() {
        return id;
    }
    public void  _getId(String theId) {
        id= theId;
    }
}
