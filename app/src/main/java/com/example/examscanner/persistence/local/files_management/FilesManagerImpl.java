package com.example.examscanner.persistence.local.files_management;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

class FilesManagerImpl implements FilesManager {
    private static final String PICTURES_DIR = "versions";
    private int counter = 0;
    private Context context;

    FilesManagerImpl(Context context){
        this.context = context;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected File getVersionsPicturesDirectoryFile() {
        String dirPath = Paths.get(
                Environment.getExternalStorageDirectory().getAbsolutePath(),
                PICTURES_DIR
        ).toString();
        return new File(dirPath);
    }

    @Override
    public void tearDown() {
        String path = Environment.getExternalStorageDirectory().toString()+"/Pictures";
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
        }
    }

    @Override
    public String genId() {
        return "GENERATED_"+String.valueOf(counter++);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Bitmap get(String path) throws FileNotFoundException {
        try {
            File f=new File(getVersionsPicturesDirectoryFile(), path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            throw e;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void store(Bitmap bm, String baseFilename) throws IOException {
        File dir = getVersionsPicturesDirectoryFile();
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, baseFilename);
        final FileOutputStream fOut = new FileOutputStream(file);
        bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        fOut.flush();
        fOut.close();
    }
}
