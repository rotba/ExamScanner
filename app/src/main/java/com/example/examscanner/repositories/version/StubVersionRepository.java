package com.example.examscanner.repositories.version;


import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class StubVersionRepository implements Repository<Version> {
    private Exam exam;

    public StubVersionRepository(Exam exam) {

        this.exam = exam;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Version get(long id) {
        return null;
    }

    @Override
    public List<Version> get(Predicate<Version> criteria) {
        ArrayList versions = new ArrayList();
        versions.add(new Version(1,exam));
        versions.add(new Version(2,exam));
        versions.add(new Version(3,exam));
        return versions;
    }

    @Override
    public void create(Version version) {
    }

    @Override
    public void update(Version version) {
    }

    @Override
    public void delete(int id) {
    }

    @Override
    public int genId() {
        return 0;
    }
}
