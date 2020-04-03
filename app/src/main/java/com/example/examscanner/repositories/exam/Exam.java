package com.example.examscanner.repositories.exam;

import androidx.annotation.NonNull;

import com.example.examscanner.repositories.grader.ExamManager;
import com.example.examscanner.repositories.grader.Grader;
import com.example.examscanner.repositories.version.Version;

import java.util.List;

public class Exam {
    private ExamManager manager;
    private long id;
    protected String courseName;
    protected int term;
    protected int semester;
    protected long[] versions;
    private List<Grader> graders;
    protected String year;
    private long sessionId;
    public Exam(ExamManager manager, List<Grader> graders, String courseName, int moed, int semester, long[] versions, long sessionId, String year) {
        this.courseName = courseName;
        this.term = moed;
        this.semester = semester;
        this.versions = versions;
        this.manager=manager;
        this.graders=graders;
        this.sessionId = sessionId;
        this.year = year;
    }

    public String getCourseName() {
        return courseName;
    }


    public long getId() {
        return id;
    }

    public boolean associatedWithGrader(int currentGraderId) {
        for (Grader g:graders) {
            if (g.getId() == currentGraderId) return true;
        }
        return false;
    }

    public int getNumOfAnswers() {
        return 53;
    }
    public void setManager(ExamManager manager) {
        this.manager = manager;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setVersions(long[] versions) {
        this.versions = versions;
    }

    public void setGraders(List<Grader> graders) {
        this.graders = graders;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }
    public ExamManager getManager() {
        return manager;
    }

    public int getTerm() {
        return term;
    }

    public int getSemester() {
        return semester;
    }

    public long[] getVersions() {
        return versions;
    }

    public List<Grader> getGraders() {
        return graders;
    }

    public String getYear() {
        return year;
    }

    public long getSessionId() {
        return sessionId;
    }

    public String getURL() {
        return "THE_EMPTY_URL";
    }

    @Override
    public String toString() {
        return String.format("%s %d %s", getCourseName(),getTerm(), getYear());
    }
}
