package com.example.examscanner.repositories.question;

public class Question {
    public Question(long id, long versionId, int ansNum, int selection, int left, int up, int right, int bottom) {
        this.id= id;
        this.versionId = versionId;
        this.num = ansNum;
        this.ans = selection;
        this.left=left;
        this.up = up;
        this.right = right;
        this.bottom = bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getUp() {
        return up;
    }

    public int getBottom() {
        return bottom;
    }

    public int getRight() {
        return right;
    }

    private long id;
    private long versionId;
    private int num;
    private int ans;
    private int left;
    private int up;
    private int bottom;
    private int right;

    public Question(long versionId, int ansNum, int selection, int left, int up, int right, int bottom) {
        this.versionId = versionId;
        this.num = ansNum;
        this.ans = selection;
        this.left=left;
        this.up = up;
        this.right = right;
        this.bottom = bottom;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVersionId() {
        return versionId;
    }

    public void setVersionId(long versionId) {
        this.versionId = versionId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getAns() {
        return ans;
    }

    public void setAns(int ans) {
        this.ans = ans;
    }
}
