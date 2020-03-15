package com.example.examscanner.image_processing;

public interface IScannedCapture {
    public int getIdentified();
    public int getUnidentified();
    public int[] getAnswers();
}
