package com.example.examscanner.authentication;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Observable;
import io.reactivex.Observer;

class FirebaseTestClaimentAuthenticationHandler implements ClaimentAuthenticationHandler<FirebaseAuth> {
    private FirebaseAuth mAuth;


    @Override
    public Observable<FirebaseAuth> generateAuthentication() {
        mAuth = FirebaseAuth.getInstance();
        return new Observable<FirebaseAuth>() {
            @Override
            protected void subscribeActual(Observer<? super FirebaseAuth> observer) {
                mAuth.signInWithEmailAndPassword("examscanner80@gmail.com", "Ycombinator")
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    observer.onNext(mAuth);
                                    observer.onComplete();
                                } else {
                                    observer.onError(task.getException());
                                }
                            }
                        });
            }
        };
    }
}
