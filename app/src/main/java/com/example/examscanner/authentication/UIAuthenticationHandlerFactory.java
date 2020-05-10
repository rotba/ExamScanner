package com.example.examscanner.authentication;

public class UIAuthenticationHandlerFactory {
    public static UIAuthenticationHandler get(){
        return new UIFirebaseAuthenticationHandler();
    }
}
