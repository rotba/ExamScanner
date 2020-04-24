package com.example.examscanner.state;

import com.example.examscanner.MainActivity;

class AuthenticatedState<T> implements State<T> {
    private T stateContent;
    public AuthenticatedState(T stateContent) {
        this.stateContent = stateContent;
    }

    @Override
    public void login(StateHolder holder, T stateContent) {}

    @Override
    public void logout(StateHolder holder) {
        holder.setState(new AnonymousState());
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public T getContent() {
        return stateContent;
    }
}
