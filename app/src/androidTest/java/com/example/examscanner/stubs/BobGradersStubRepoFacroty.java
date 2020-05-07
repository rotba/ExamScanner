package com.example.examscanner.stubs;

import androidx.test.espresso.core.internal.deps.guava.collect.Lists;

import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.grader.Grader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BobGradersStubRepoFacroty {
    public static Repository<Grader> get() {
        return new Repository<Grader>() {
            private List<Grader> graders = new ArrayList<Grader>(){{
                add(new Grader("bob"));
            }};
            @Override
            public int getId() {
                throw new NotImplementedException();
            }

            @Override
            public Grader get(long id) {
                throw new NotImplementedException();
            }

            @Override
            public List<Grader> get(Predicate<Grader> criteria) {
                return graders;
            }

            @Override
            public void create(Grader grader) {
                throw new NotImplementedException();
            }

            @Override
            public void update(Grader grader) {
                throw new NotImplementedException();
            }

            @Override
            public void delete(int id) {
                throw new NotImplementedException();
            }

            @Override
            public int genId() {
                throw new NotImplementedException();
            }
        };
    }

    public static class NotImplementedException extends RuntimeException {}
}
