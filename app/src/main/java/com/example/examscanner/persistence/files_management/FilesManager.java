package com.example.examscanner.persistence.files_management;

import android.graphics.Bitmap;

public interface FilesManager {
    public Bitmap get(int id);
    public void store(Bitmap bm, int id);
}
