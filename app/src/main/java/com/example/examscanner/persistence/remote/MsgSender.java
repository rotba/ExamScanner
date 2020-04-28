package com.example.examscanner.persistence.remote;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.reactivex.Completable;

public class MsgSender {
    public Completable send(String toRef, String msg) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("http://10.0.2.2:9000?ns=examscanner-de46e");
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(toRef);
        myRef.setValue(msg);
        return Completable.fromAction(() -> {
            Task t  = myRef.setValue(msg);
            if(!t.isSuccessful())
                throw t.getException();
        });

    }
}
