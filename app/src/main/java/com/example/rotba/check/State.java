package com.example.rotba.check;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.example.rotba.check.R;
import com.example.rotba.check.controllers.AuthenticationActivity;
import com.example.rotba.check.controllers.HomeActivity;

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
    public abstract Intent getInitialActivityIntent(AppCompatActivity curAc);

    public void login(){instance = new LoggedInState();};

    private static class AnonymousState extends State{
        @Override
        public Intent getInitialActivityIntent(AppCompatActivity curAc) {
            return new Intent(curAc, AuthenticationActivity.class);
        }
    }
    private static class LoggedInState extends  State{
        @Override
        public Intent getInitialActivityIntent(AppCompatActivity curAc) {
            return new Intent(curAc, HomeActivity.class);
        }
    }
}
