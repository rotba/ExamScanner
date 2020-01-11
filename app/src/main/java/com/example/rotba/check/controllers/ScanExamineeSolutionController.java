package com.example.rotba.check.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rotba.check.R;
import com.example.rotba.check.model.Facade;
import com.example.rotba.check.model.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class ScanExamineeSolutionController extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private List<Integer> checks = new ArrayList<>();
    private RandomNumberGenerator gen = new RandomNumberGenerator();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Integer checkId = gen.get();
            try {
                checks.add(checkId);
                Facade.getInstance().scanExaminneSolution(imageBitmap, checkId);
            } catch (NotImplementedException e) {
                e.printStackTrace();
            }
        }
    }

    private class RandomNumberGenerator {
        private int curr = 0;
        public int get(){
            int ans = curr;
            curr++;
            return ans;
        }
    }
}
