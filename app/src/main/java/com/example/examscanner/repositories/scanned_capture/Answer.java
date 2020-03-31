package com.example.examscanner.repositories.scanned_capture;

import java.util.List;

public abstract class Answer {

    private final int ansNum;

    public Answer(int id) {
        this.ansNum = id;
    }
    public int getAnsNum(){return ansNum;}
    public boolean isChecked(){
        return false;
    }
    public boolean isConflicted(){
        return false;
    }
    public boolean isResolvedConflictedMissing(){
        return false;
    }
    public boolean isMissing(){
        return false;
    }
    public Answer commitResolution(){return this;};
    public void addMe(List<ConflictedAnswer> l){return;};

}
