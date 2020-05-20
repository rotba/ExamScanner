package com.example.examscanner.repositories.grader;

public class Grader {
    private String userName;
    private String userId;

    public Grader(String userName, String userId) {
        this.userName = userName;
        this.userId  =userId;
    }


    public String getUserName() {
        return userName;
    }

    public String getIdentifier(){
        return userId;
    }
}
