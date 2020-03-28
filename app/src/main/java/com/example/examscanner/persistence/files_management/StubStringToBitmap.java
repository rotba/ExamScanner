package com.example.examscanner.persistence.files_management;

import android.graphics.Bitmap;

import java.util.HashMap;

class StubFilesManager implements FilesManager {
    private HashMap<Integer, Bitmap> map = new HashMap<>();
    @Override
    public Bitmap get(int id) {
        return map.get(id);
    }

    @Override
    public void store(Bitmap bm, int id) {
        map.put(id, bm);
    }
}
