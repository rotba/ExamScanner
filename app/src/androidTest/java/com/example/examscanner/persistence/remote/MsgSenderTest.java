package com.example.examscanner.persistence.remote;

import com.example.examscanner.StateFullTest;

import org.junit.Test;

import io.reactivex.Observable;

import static org.junit.Assert.*;

public class MsgSenderTest extends StateFullTest {

    public static final String TEST_REF = "TEST_REF";
    public static final String TEST_MSG = "TEST_MSG";

    @Test
    public void sendAndRead() {
        new MsgSender().send(TEST_REF, TEST_MSG);
        new MsgReader().read(TEST_REF).subscribe(
                s->assertEquals(s,TEST_MSG),
                t -> fail()
        );
    }
}