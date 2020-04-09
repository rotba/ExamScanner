package com.example.examscanner.components.create_exam.get_version_file;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.fragment.app.FragmentActivity;

public interface VersionImageGetter {
    public void get(FragmentActivity activity);
    public Bitmap accessBitmap(Intent data, FragmentActivity activity);
}
