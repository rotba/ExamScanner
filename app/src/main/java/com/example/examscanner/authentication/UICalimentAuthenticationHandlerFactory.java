package com.example.examscanner.authentication;

public class UICalimentAuthenticationHandlerFactory {
    public static UIClaimentAuthenticationHandler get(){
        return new UIFirebaseClaimentAuthenticationHandler();
    }
}
