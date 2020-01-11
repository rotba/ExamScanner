package com.example.rotba.check;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.rotba.check.R;
import com.example.rotba.check.controllers.ScanExamineeSolutionController;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void scanExamineeSolution(View view){
        Intent intent = new Intent(this, ScanExamineeSolutionController.class);
        startActivity(intent);
    }
}
