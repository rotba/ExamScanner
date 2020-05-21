package com.example.examscanner.repositories.scanned_capture;

import androidx.annotation.NonNull;

import java.util.List;

public class MissingAnswer extends Answer{
    public MissingAnswer(int id) {
        super(id);
    }

    @Override
    public boolean isMissing() {
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        return "M";
    }
}
