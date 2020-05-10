package com.example.examscanner.authentication;

public class AuthenticationHandlerFactory {
    public static AuthenticationHandler getTest(){
        return new FirebaseAuthenticationHandler();
    }
}
