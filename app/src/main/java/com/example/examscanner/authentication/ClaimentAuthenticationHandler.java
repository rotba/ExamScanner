package com.example.examscanner.authentication;

import android.content.Intent;

import io.reactivex.Observable;

public interface ClaimentAuthenticationHandler<T> {
    public Observable<T> generateAuthentication();
    public Observable<String> generateAuthenticationAndReturnId();
}
