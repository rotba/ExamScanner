package com.example.examscanner.communication;

public interface StatisticsSourceStub {

    void setAvg(double someRandomeAvg);

    void setVar(double someRandomVariance);

    void setNumOfSolutions(int someRandomNumOfSolutions);

    double getAvg();

    double getVar();

    int getNumOfCheckedSolutions();
}
