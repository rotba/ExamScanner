package com.example.examscanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AuthenticationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_authentication);
        initializeEventHandlers();
    }

    private void initializeEventHandlers() {
        Button logginB = (Button)findViewById(R.id.button_login);
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
            authActivity.startActivity(new Intent(authActivity, MainActivity.class));
        }
    }
}
