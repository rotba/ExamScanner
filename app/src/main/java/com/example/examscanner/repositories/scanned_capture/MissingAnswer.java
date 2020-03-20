package com.example.examscanner.repositories.scanned_capture;

import java.util.List;

public class MissingAnswer extends Answer{
    public MissingAnswer(int id) {
        super(id);
    }

    @Override
    public boolean isMissing() {
        return true;
    }

    @Override
    public void addMe(List<ConflictedAnswer> l) {}
}
