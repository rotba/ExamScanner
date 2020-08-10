package com.example.examscanner.log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

class FirebaseESLogger implements ESLogger {

    @Override
    public void setupUserIdentifier(String identifier) {
        FirebaseCrashlytics.getInstance().setUserId(identifier);
    }

    @Override
    public void log(String tag, String message, Throwable e) {
        FirebaseCrashlytics.getInstance().setCustomKey("TAG", tag);
        FirebaseCrashlytics.getInstance().log(message);
        FirebaseCrashlytics.getInstance().recordException(e);
    }

    @Override
    public void log(String tag, String format) {
        FirebaseCrashlytics.getInstance().setCustomKey("TAG", tag);
        FirebaseCrashlytics.getInstance().log(format);
    }
}
