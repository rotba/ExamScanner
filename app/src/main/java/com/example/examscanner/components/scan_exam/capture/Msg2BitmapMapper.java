package com.example.examscanner.components.scan_exam.capture;


import android.graphics.Bitmap;
import android.os.Message;

public interface Msg2BitmapMapper {
    public Bitmap map(Message m);
}
