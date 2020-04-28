package com.example.examscanner.authentication;

public class CalimentAuthenticationHandlerFactory {
    public static ClaimentAuthenticationHandler getTest(){
        return new FirebaseTestClaimentAuthenticationHandler();
    }
}
