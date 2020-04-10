package com.example.examscanner.persistence.files_management;

public class FilesManagerFactory {
    public static FilesManager create(){
        return new StubFilesManager();
    }
}
