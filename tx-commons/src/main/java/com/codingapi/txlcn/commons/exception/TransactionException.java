package com.codingapi.txlcn.commons.exception;

/**
 * Description: 事务异常
 * Date: 19-1-11 下午6:16
 *
 * @author ujued
 */
public class TransactionException extends Exception {
    public TransactionException() {
    }

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }

    public TransactionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
