package com.codingapi.tx.commons.exception;

/**
 * Description:
 * Date: 19-1-14 上午9:58
 *
 * @author ujued
 */
public class TxcLogicException extends Exception {
    public TxcLogicException() {
    }

    public TxcLogicException(String message) {
        super(message);
    }

    public TxcLogicException(String message, Throwable cause) {
        super(message, cause);
    }

    public TxcLogicException(Throwable cause) {
        super(cause);
    }

    public TxcLogicException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
