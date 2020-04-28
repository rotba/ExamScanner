package com.example.examscanner.authentication.state;

class AnonymousState<T> implements State<T> {
    @Override
    public void login(StateHolder holder, T stateContent) {
        holder.setState(new AuthenticatedState<T>(stateContent));
    }

    @Override
    public void logout(StateHolder holder) {
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public T getContent() {
        return null;
    }
}
