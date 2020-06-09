package com.example.examscanner.image_processing;

class CummulatedAdjustment {
    private int prevX;
    private int prevY;
    private int currXAdj;
    private int currYAdj;
    private static int LARGE_X_DISTANCE = 50;

    public static CummulatedAdjustment get() {
        return new CummulatedAdjustment();
    }

    public CummulatedAdjustment() {
        prevX = -1;
        prevY = -1;
        currXAdj = 0;
        currYAdj = 0;
    }

    public void getNext(int leftMostX, int upperMostY) {
        if (upperMostY < prevY) {
            currYAdj = 0;
        }
        if (Math.abs(leftMostX - prevX) > LARGE_X_DISTANCE) {
            currXAdj = 0;
        }
        prevY = upperMostY;
        prevX = leftMostX;
    }

    public void accumulateX(int x) {
        currXAdj += x;
    }

    public int getCurrXAdj() {
        return currXAdj;
    }

    public int getCurrYAdj() {
        return currYAdj;
    }

    public void accumulateY(int y) {
        currYAdj += y;
    }
}

