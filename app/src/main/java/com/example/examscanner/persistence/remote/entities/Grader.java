package com.example.examscanner.persistence.remote.entities;

public class Grader {
    public String userId;
    private String username;

    public Grader(String id, String username) {
        this.userId = id;
        this.username = username;
    }

    public String _getUserName(){
        return username;
    }
}
