package com.example.examscanner.repositories.exam;

import android.util.Log;


import androidx.annotation.Nullable;

import com.example.examscanner.repositories.grader.Grader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Exam {
    private final static String TAG = "ExamScanner";
    private final static String MSG_PREF = "Exam::";
    protected String managerId;
    private long id;
    protected String courseName;
    protected int term;
    protected int semester;
    protected List<Grader> graders;
    protected String year;
    private long sessionId;
    private List<Version> newVersions;
//    private List<Version> cachedVersions;
    private Future<List<Version>> fVersions;
    private int numOfQuestions;
//    private boolean doResolveFutures;

    public Exam(String managerId,long id, Future<List<Version>> fVersions, List<Grader> graders, String courseName, int moed, int semester, long sessionId, String year, int numOfQuestions) {
        this.id = id;
        this.courseName = courseName;
        this.term = moed;
        this.semester = semester;
        this.graders = graders;
        this.sessionId = sessionId;
        this.year = year;
        this.fVersions = fVersions;
        newVersions = new ArrayList<>();
//        cachedVersions = new ArrayList<>();
        this.numOfQuestions = numOfQuestions;
//        doResolveFutures =true;
    }

    public void setFutureVersions(Future<List<Version>> fVersions) {
        this.fVersions = fVersions;
    }

    public String getCourseName() {
        return courseName;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setGraders(List<Grader> graders) {
        this.graders = graders;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public int getTerm() {
        return term;
    }

    public int getSemester() {
        return semester;
    }


    public List<Grader> getGraders() {
        return graders;
    }

    public String getYear() {
        return year;
    }

    public long getSessionId() {
        return sessionId;
    }

    public String getURL() {
        return "THE_EMPTY_URL";
    }

    @Override
    public String toString() {
        return String.format("%s %d %s", getCourseName(), getTerm(), getYear());
    }

    public Version getVersionByNum(int verNum) {
        for (Version v : accessVersionFuture()) {
            if (v.getNum() == verNum)
                return v;
        }
        throw new NuSuchVerion();
    }

    public List<Version> getVersions() {
        List<Version> ans = new ArrayList<>();
        ans.addAll(newVersions);
        ans.addAll(accessVersionFuture());
        return ans;
    }

    private List<Version> accessVersionFuture() {
//        if(!doResolveFutures){
//            return new ArrayList<>();
//        }
        try {
            return fVersions.get();
        } catch (ExecutionException e) {
            Log.d(TAG, MSG_PREF + " getVersionByNum",e);
            e.printStackTrace();
            throw new RuntimeException("Problem with furure");
        } catch (InterruptedException e) {
            Log.d(TAG, MSG_PREF + " getVersionByNum",e);
            e.printStackTrace();
            throw new RuntimeException("Problem with furure");
        }
    }

    public void addVersion(Version v) {
        newVersions.add(v);
    }

    public String getManagerId() {
        return managerId;
    }

    public String[] getGradersIdentifiers() {
        String[] ans = new String[graders.size()];
        for (int i = 0; i <ans.length ; i++) {
            ans[i] = graders.get(i).getIdentifier();
        }
        return ans;
    }

    public void setNumOfQuestions(int num) {
        this.numOfQuestions = num;
    }

    public int getNumOfQuestions() {
        return numOfQuestions;
    }

    public class NuSuchVerion extends RuntimeException {




    }
    public static Future<List<Version>> theErrorFutureVersionsList() {
        return new Future<List<Version>>() {
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
                return false;
            }

            @Override
            public List<Version> get() throws ExecutionException, InterruptedException {
                throw new RuntimeException("Bug in exam scanner. Probably Versions future was not set");
            }

            @Override
            public List<Version> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return null;
            }
        };
    }
    public static Future<List<Version>> theEmptyFutureVersionsList() {
        return new Future<List<Version>>() {
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
                return new ArrayList<>();
            }

            @Override
            public List<Version> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return null;
            }
        };
    }
    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Exam))
            return false;
        Exam other = (Exam)obj;
        boolean ans = true;
        ans &= getCourseName().equals(other.getCourseName());
        ans &= getYear().equals(other.getYear());
        ans &= getSemester() == other.getSemester();
        List<Version> otherVersions = other.getVersions();
        ans &= otherVersions.size() == getVersions().size();
        for (Version ver:
             getVersions()) {
            ans&= otherVersions.contains(ver);
        };
        return ans;
    }
    public void dontResoveFutures() {
//        doResolveFutures = false;
//        for (Version v:getVersions()) {
//            v.dontResolveFutures();
//        }
    }

    public void quziEagerLoad() {
        for (Version v:getVersions()) {
            v.quziEagerLoad();
        }
    }
}
