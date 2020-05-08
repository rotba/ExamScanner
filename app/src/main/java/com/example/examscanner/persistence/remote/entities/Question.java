package com.example.examscanner.persistence.remote.entities;

public class Question {
    public int num;
    public int ans;
    public int left;
    public int up;
    public int right;
    public String versionId;
    public int bottom;

    public Question(int num, int ans, int left, int up, int right, String versionId, int bottom) {
        this.num = num;
        this.ans = ans;
        this.left = left;
        this.up = up;
        this.right = right;
        this.versionId = versionId;
        this.bottom = bottom;
    }
}
