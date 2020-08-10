package com.example.examscanner.log;

import android.util.Log;

class EsLoggeImpl implements ESLogger {
    FirebaseESLogger remoteLogger;
    public EsLoggeImpl() {
        remoteLogger = new FirebaseESLogger();
    }

    @Override
    public void setupUserIdentifier(String identifier) {
        remoteLogger.setupUserIdentifier(identifier);
    }

    @Override
    public void log(String tag, String message, Exception e) {
        Log.d(tag, message, e);
        remoteLogger.log(tag, message, e);
    }
}
