package com.example.examscanner;

import android.content.Intent;

import androidx.lifecycle.ViewModel;

import com.example.examscanner.authentication.UIAuthenticationHandler;
import com.example.examscanner.authentication.state.State;
import com.example.examscanner.authentication.state.StateHolder;

public class MainActivityViewModel<T> extends ViewModel implements StateHolder {

    private final UIAuthenticationHandler tUIAuthenticationHandler;
    private State theState;



    public MainActivityViewModel(State theState, UIAuthenticationHandler<T> tUIAuthenticationHandler) {
        this.tUIAuthenticationHandler = tUIAuthenticationHandler;
        this.theState = theState;
    }

    public boolean isAuthenticated() {
        return theState.isAuthenticated();
    }

    public void authenticate() {
        theState.login(this, tUIAuthenticationHandler.getOnResultContent());
    }

    @Override
    public void setState(State s) {
        this.theState = s;
    }

    public Object getState() {
        return theState.getContent();
    }

    public Intent generateAuthenticationIntent() {
        return tUIAuthenticationHandler.generateAuthenticationIntent();
    }
}
