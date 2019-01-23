package com.codingapi.txlcn.commons.exception;

/**
 * Description:
 * Date: 19-1-23 下午6:03
 *
 * @author ujued
 */
public class LcnBusinessException extends Exception {
    public LcnBusinessException() {
    }

    public LcnBusinessException(String message) {
        super(message);
    }

    public LcnBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public LcnBusinessException(Throwable cause) {
        super(cause);
    }

    public LcnBusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
