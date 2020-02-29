package com.example.examscanner;


public abstract class State {
    private static State instance;
    public static State getState(){
        if (instance != null) {
            return instance;
        }else{
            instance = new AnonymousState();
            return instance;
        }
    }

    public void login(){instance = new LoggedInState();};

    public abstract void onInitialCreate(MainActivity mainActivity);

    public int getGraderId() {
        return 0;
    }

    private static class AnonymousState extends State{
        @Override
        public void onInitialCreate(MainActivity mainActivity) {
            mainActivity.navigateToAuthentication();
        }
    }
    private static class LoggedInState extends  State{
        @Override
        public void onInitialCreate(MainActivity mainActivity) {
            mainActivity.createHome();
        }
    }
}
