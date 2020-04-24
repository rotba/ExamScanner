package com.example.examscanner.state;

public interface State {
    public <T> void login(StateHolder holder, T stateContent);
    public void logout(StateHolder holder);
    public boolean isAuthenticated();
}
