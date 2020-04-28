package com.example.examscanner.authentication;

import android.content.Intent;

public interface UIClaimentAuthenticationHandler<T> {
    public Intent generateAuthenticationIntent();
    public T getAuthenticationContent();
}
