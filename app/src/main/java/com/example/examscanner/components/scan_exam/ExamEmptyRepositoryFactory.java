package com.example.examscanner.components.scan_exam;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ExamEmptyRepositoryFactory {

    public static Repository<Exam> create() {
        return  new Repository<Exam>() {
            List<Exam> exams = new ArrayList<>();
            @Override
            public int getId() {
                return 0;
            }

            @Override
            public Exam get(long id) {
                return null;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public List<Exam> get(Predicate<Exam> criteria) {
                return exams.stream().filter(criteria).collect(Collectors.toList());
            }

            @Override
            public void create(Exam exam) {
                exams.add(exam);
            }

            @Override
            public void update(Exam exam) {

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
