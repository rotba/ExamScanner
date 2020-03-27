package com.example.examscanner.components.scan_exam.capture;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;

import com.example.examscanner.stubs.BitmapInstancesFactory;

public class StubMsg2BitmapMapper implements Msg2BitmapMapper {
    private BitmapInstancesFactory bmFact;
    public StubMsg2BitmapMapper(Context context) {
        bmFact = new BitmapInstancesFactory(context);
    }

    @Override
    public Bitmap map(Message m) {
        return bmFact.getRandom();
    }
}
