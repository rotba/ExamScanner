package com.example.examscanner.persistence.remote.files_management;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.file.Paths;

public class PathsGenerator {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String genVersionPath(String examId, int verNum){
        return Paths.get(examId, String.valueOf(verNum)).toString();
    }
}
