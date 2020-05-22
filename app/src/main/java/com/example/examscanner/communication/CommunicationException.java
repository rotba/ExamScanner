package com.example.examscanner.communication;

class CommunicationException extends RuntimeException {
    public CommunicationException(Throwable cause) {
        super(cause);
    }

    public CommunicationException(String s) {
        super(s);
    }
}
