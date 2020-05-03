package com.example.examscanner.authentication.state;

import com.google.firebase.auth.FirebaseAuth;

class FirebaseState implements State<FirebaseAuth> {
    private FirebaseAuth stateContent;

    public FirebaseState(FirebaseAuth stateContent) {
        this.stateContent = stateContent;
    }

    @Override
    public void login(StateHolder holder, FirebaseAuth stateContent) {}

    @Override
    public void logout(StateHolder holder) {
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public String getId() {
        return stateContent.getUid();
    }

    @Override
    public FirebaseAuth getContent() {
        return null;
    }
}
