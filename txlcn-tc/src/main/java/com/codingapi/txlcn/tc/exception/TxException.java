package com.codingapi.txlcn.tc.exception;

public class TxException extends RuntimeException {

    public TxException() {
    }

    public TxException(String s) {
        super(s);
    }

    public TxException(String message, Throwable cause) {
        super(message, cause);
    }

    public TxException(Throwable cause) {
        super(cause);
    }
}
