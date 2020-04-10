package com.example.examscanner.components.scan_exam.capture;

import android.content.Context;

public class Msg2BitmapFactory {
    public Context c;
    public Msg2BitmapFactory(Context c) {
        this.c=c;
    }

    public Msg2BitmapMapper create(){
        return new StubMsg2BitmapMapper(c);
    }
}
