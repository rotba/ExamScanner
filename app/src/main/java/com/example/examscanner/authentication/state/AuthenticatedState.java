package com.example.examscanner.authentication.state;

class AuthenticatedState<T> implements State<T> {
    private T stateContent;
    public AuthenticatedState(T stateContent) {
        this.stateContent = stateContent;
    }

    @Override
    public void login(StateHolder holder, T stateContent) {}

    @Override
    public void logout(StateHolder holder) {
        holder.setState(new FirebaseAnonymousState());
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public T getContent() {
        return stateContent;
    }
}
