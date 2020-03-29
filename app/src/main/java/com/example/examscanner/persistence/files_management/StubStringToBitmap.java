package com.example.examscanner.persistence.files_management;

import android.graphics.Bitmap;

import java.util.HashMap;

class StubFilesManager implements FilesManager {
    private static long counter = 0;
    private HashMap<Long, Bitmap> map = new HashMap<>();

    @Override
    public Bitmap get(long id) {
        return map.get(id);
    }

    @Override
    public long store(Bitmap bm) {
        long id = counter++;
        map.put(id, bm);
        return id;
    }
}
