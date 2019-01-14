package com.codingapi.txlcn.commons.exception;

/**
 * Description:
 * Date: 1/8/19
 *
 * @author ujued
 */
public class TransactionStateException extends Exception {
    public static final int NON_MOD = 10;
    public static final int NON_ASPECT = 11;
    public static final int RPC_ERR = 12;

    private int code;

    public TransactionStateException(String message, int code) {
        super(message);
        this.code = code;
    }

    public TransactionStateException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public TransactionStateException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public TransactionStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
