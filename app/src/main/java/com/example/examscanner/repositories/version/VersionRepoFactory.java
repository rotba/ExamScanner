package com.example.examscanner.repositories.version;

import com.example.examscanner.repositories.Repository;

public class VersionRepoFactory {
    public Repository<Version> create(){
        return VersionRepository.getInstance();
    }
}
