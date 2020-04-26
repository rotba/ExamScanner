package com.example.examscanner.persistence.remote;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MsgSender {
    public void send(String toRef,String msg){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(toRef);
        myRef.setValue(msg);
    }
}
