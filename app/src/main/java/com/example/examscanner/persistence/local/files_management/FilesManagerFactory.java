package com.example.examscanner.persistence.local.files_management;

public class FilesManagerFactory {
    public static FilesManager create(){
        return new StubFilesManager();
    }
}
