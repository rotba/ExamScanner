package com.example.examscanner.persistence.files_management;

import android.graphics.Bitmap;

public interface FilesManager {
    public Bitmap get(long id);
    public long store(Bitmap bm);
}
