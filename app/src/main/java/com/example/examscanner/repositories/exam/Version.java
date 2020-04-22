package com.example.examscanner.repositories.exam;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Version {
    private static final String TAG = "ExamScanner";
    private static final String MSG_PREF = "Version";
    private Exam exam;
    private long id;
    private int num;
    private Future<List<Question>> fQuestions;
    private List<Question> newQuestions;


    public Version(long id,int num, Exam e, Future<List<Question>> fQuestions) {
        this.id=id;
        this.num = num;
        this.exam = e;
        newQuestions = new ArrayList<>();
        this.fQuestions = fQuestions;
    }

    public int getNum() {
        return num;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Exam getExam() {
        return exam;
    }

    public void addQuestion(Question question) {
        newQuestions.add(question);
    }

    public List<Question> getQuestions() {
        List<Question> ans = new ArrayList<>();
        ans.addAll(newQuestions);
        ans.addAll(accessFQuestions());
        return ans;
    }

    private List<Question> accessFQuestions() {
        try {
            return fQuestions.get();
        } catch (ExecutionException e) {
            Log.d(TAG,MSG_PREF+ " accessFQuestions");
            e.printStackTrace();
            throw new RuntimeException("Problem with future");
        } catch (InterruptedException e) {
            Log.d(TAG,MSG_PREF+ " accessFQuestions");
            e.printStackTrace();
            throw new RuntimeException("Problem with future");
        }
    }

    public static Future<List<Question>> theEmptyFutureQuestionsList(){
        return new Future<List<Question>>() {
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
            public List<Question> get() throws ExecutionException, InterruptedException {
                return new ArrayList<>();
            }

            @Override
            public List<Question> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return null;
            }
        };
    }

    public static Future<List<Question>> theErrorFutureQuestionsList() {
        return new Future<List<Question>>() {
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
            public List<Question> get() throws ExecutionException, InterruptedException {
                throw new RuntimeException("Bug in exam scanner. Probably Questions future was not set");
            }

            @Override
            public List<Question> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return null;
            }
        };
    }

    public void setQuestionsFuture(Future<List<Question>> questionsFuture) {
        this.fQuestions = questionsFuture;
    }
}
