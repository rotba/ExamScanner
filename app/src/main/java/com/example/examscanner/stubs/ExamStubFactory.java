package com.example.examscanner.stubs;


import android.graphics.Bitmap;

import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.Version;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExamStubFactory {
    public static final int instance1_dinaBarzilayVersion = 496351;
    public static final int instance1_theDevilVersion = 666;
    private static long instance1Id = -800;
    public static String instance1_courseName = "TEST_courseName";
    private static long sessionId = -900;

    public static Exam instance1() {
        Exam e = new Exam(
                null,
                instance1Id,
                Exam.theErrorFutureVersionsList(),
                new ArrayList<>(),
                instance1_courseName,
                0,
                1,
                sessionId,
                "2020"
        );
        Future<List<Version>> fv = new Future<List<Version>>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return true;
            }

            @Override
            public List<Version> get() throws ExecutionException, InterruptedException {
                return new ArrayList<Version>(){{
                    add(new Version(-1,instance1_dinaBarzilayVersion, e, Version.theEmptyFutureQuestionsList(),null));
                    add(new Version(-1,instance1_theDevilVersion, e, Version.theEmptyFutureQuestionsList(),null));
                }};
            }

            @Override
            public List<Version> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return null;
            }
        };
        e.setFutureVersions(fv);
        return e;
    }
}
