package com.example.examscanner.state;

public interface State<T> {
    public void login(StateHolder holder, T stateContent);
    public void logout(StateHolder holder);
    public boolean isAuthenticated();
    T getContent();
}
