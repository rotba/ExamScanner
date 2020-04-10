package com.example.examscanner.repositories;

import com.example.examscanner.repositories.version.Version;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class VersionRepoStubFactory {
    public static Repository<Version> createStubThatReturns(ArrayList<Version> arrayList) {
        return new Repository<Version>() {
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
                return arrayList;
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
        };
    }
}
