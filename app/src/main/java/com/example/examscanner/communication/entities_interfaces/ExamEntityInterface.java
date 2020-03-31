package com.example.examscanner.communication.entities_interfaces;

public interface ExamEntityInterface {
    public long getID();
    public long[] getVersionsIds();
    public String getCourseName();
    public String getUrl();
    public String getYear();
    public int getTerm();
    public long getSessionId();
    public int getSemester();
}
