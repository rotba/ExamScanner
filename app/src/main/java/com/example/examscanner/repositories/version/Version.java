package com.example.examscanner.repositories.version;

import com.example.examscanner.repositories.exam.Exam;

public class Version {
    private long eId;
    private int[] answers;
    private long id;
    private int num;

    public Version(int num,long eId, int[] answers) {
        this.eId = eId;
        this.answers = answers;
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long getExamId() {
        return eId;
    }
}
