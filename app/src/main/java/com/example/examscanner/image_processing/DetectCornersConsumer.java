package com.example.examscanner.image_processing;

import android.graphics.Point;

public interface DetectCornersConsumer {
    public void consume(Point upperLeft, Point upperRight, Point bottomLeft, Point bottomRight);
}
