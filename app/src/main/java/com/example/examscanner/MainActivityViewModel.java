package com.example.examscanner;

import android.content.Intent;

import androidx.lifecycle.ViewModel;

import com.example.examscanner.authentication.UIClaimentAuthenticationHandler;
import com.example.examscanner.authentication.state.State;
import com.example.examscanner.authentication.state.StateHolder;

public class MainActivityViewModel<T> extends ViewModel implements StateHolder {

    private final UIClaimentAuthenticationHandler tUIClaimentAuthenticationHandler;
    private State theState;



    public MainActivityViewModel(State theState, UIClaimentAuthenticationHandler<T> tUIClaimentAuthenticationHandler) {
        this.tUIClaimentAuthenticationHandler = tUIClaimentAuthenticationHandler;
        this.theState = theState;
    }

    public boolean isAuthenticated() {
        return theState.isAuthenticated();
    }

    public void authenticate() {
        theState.login(this, tUIClaimentAuthenticationHandler.getAuthenticationContent());
    }

    @Override
    public void setState(State s) {
        this.theState = s;
    }

    public Object getState() {
        return theState.getContent();
    }

    public Intent generateAuthenticationIntent() {
        return tUIClaimentAuthenticationHandler.generateAuthenticationIntent();
    }
}
