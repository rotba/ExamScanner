package com.example.examscanner.authentication;

import android.content.Intent;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

class UIFirebaseAuthenticationHandler implements UIAuthenticationHandler<FirebaseAuth> {
    @Override
    public Intent generateAuthenticationIntent() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build();
    }

    @Override
    public FirebaseAuth getOnResultContent() {
        return FirebaseAuth.getInstance();
    }
}
