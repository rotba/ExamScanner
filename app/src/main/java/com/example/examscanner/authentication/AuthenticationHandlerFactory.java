package com.example.examscanner.authentication;

public class AuthenticationHandlerFactory {
    public static AuthenticationHandler getTest(){
        return new FirebaseAuthenticationHandler();
    }
    public static AuthenticationHandler getBobTest(){
        return new FirebaseBobAuthenticationHandler();
    }
    public static AuthenticationHandler getAliceTest(){
        return new FirebaseAliceAuthenticationHandler();
    }
}
