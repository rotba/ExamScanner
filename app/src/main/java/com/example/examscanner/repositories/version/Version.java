package com.example.examscanner.repositories.version;

public class Version {
    private long eId;
    private long[] answers;
    private long id;
    private int num;

    public Version(int num, long eId, long[] answers) {
        this.eId = eId;
        this.answers = answers;
        this.num = num;
    }

    public Version(long id, int number, long examId, long[] questions) {
        this.id=id;
        this.eId = examId;
        this.answers = questions;
        this.num = number;
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

    public long[] getQuestions() {
        return answers;
    }
}
