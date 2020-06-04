package com.example.examscanner.stubs;

public class AStatisticsSourceStub implements com.example.examscanner.communication.StatisticsSourceStub {
    double avg;
    double var;
    int checkedSoltuions;
    @Override
    public void setAvg(double someRandomeAvg) {
        avg=someRandomeAvg;
    }

    @Override
    public void setVar(double someRandomVariance) {
        var=someRandomVariance;
    }

    @Override
    public void setNumOfSolutions(int someRandomNumOfSolutions) {
        checkedSoltuions = someRandomNumOfSolutions;
    }

    @Override
    public double getAvg() {
        return avg;
    }

    @Override
    public double getVar() {
        return var;
    }

    @Override
    public int getNumOfCheckedSolutions() {
        return checkedSoltuions;
    }
}
