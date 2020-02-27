package com.example.rotba.check.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.rotba.check.R;
import com.example.rotba.check.State;

public class AuthenticationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeEventHandlers();
    }

    private void initializeEventHandlers() {
        Button logginB = (Button)findViewById(R.id.login_button);
        logginB.setOnClickListener(new LogginHandler(this));
    }


    private class LogginHandler implements View.OnClickListener{
        private AuthenticationActivity authActivity;
        public LogginHandler(AuthenticationActivity a) {
            authActivity = a;
        }

        @Override
        public void onClick(View view) {
            State.getState().login();
            authActivity.startActivity(new Intent(authActivity, HomeActivity.class));
        }
    }
}
