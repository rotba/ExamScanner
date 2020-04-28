package com.example.examscanner.persistence.remote;

import com.example.examscanner.StateFullTest;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executor;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class MsgSenderTest {

    public static final String TEST_REF = "TEST_REF";
    public static final String TEST_MSG = "TEST_MSG";

    @Before
    public void setUp() throws Exception {
//        FirebaseApp app =FirebaseApp.initializeApp(getInstrumentation().getContext());
//        FirebaseAuth.getInstance(app).signInAnonymously();
    }

    @Test
    public void sendAndRead() throws InterruptedException {

        new MsgSender().send(TEST_REF, TEST_MSG).subscribe(
                ()->{},
                t-> fail()
        );

        new MsgReader().read(TEST_REF).subscribe(
                s->assertEquals(TEST_MSG,s),
                t->fail()
        );
    }
}