package com.example.examscanner.repositories.exam;

import androidx.annotation.Nullable;

import com.example.examscanner.repositories.exam.Version;

public class Question {
    private long id;
    private Version version;
    private int num;
    private int ans;
    private int left;
    private int up;
    private int bottom;
    private int right;

    public Question(long qId, Version v, int ansNum, int selection, int left, int up, int right, int bottom) {
        this.version = v;
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

    public Question(long versionId, int ansNum, int selection, int left, int up, int right, int bottom) {
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
        return version.getId();
    }

    public void setVersionId(long versionId) {

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

    @Override
    public boolean equals(@Nullable Object obj) {
        if(!(obj instanceof  Question)){
            return false;
        }
        Question other = (Question)obj;
        boolean ans =true;
        ans&= getNum()==other.getNum();
        ans&= getAns()==other.getAns();
        ans&= getUp() == other.getUp();
        ans&= getBottom() == other.getBottom();
        ans&= getRight() == other.getRight();
        return ans;
    }
}
