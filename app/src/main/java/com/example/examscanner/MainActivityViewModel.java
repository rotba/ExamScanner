package com.example.examscanner;

import androidx.lifecycle.ViewModel;

import com.example.examscanner.state.State;
import com.example.examscanner.state.StateHolder;

public class MainActivityViewModel extends ViewModel implements StateHolder {

    private State theState;

    public MainActivityViewModel(State theState) {
        this.theState = theState;
    }

    public boolean isAuthenticated() {
        return theState.isAuthenticated();
    }

    public <T> void authenticate(T stateContent) {
        theState.login(this, stateContent);
    }

    @Override
    public void setState(State s) {
        this.theState = s;
    }

    public Object getState() {
        return theState.getContent();
    }
}
