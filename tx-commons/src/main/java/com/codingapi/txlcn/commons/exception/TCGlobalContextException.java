package com.codingapi.txlcn.commons.exception;

/**
 * Description:
 * Date: 19-1-23 下午1:07
 *
 * @author ujued
 */
public class TCGlobalContextException extends Exception {
    public TCGlobalContextException() {
    }

    public TCGlobalContextException(String message) {
        super(message);
    }

    public TCGlobalContextException(String message, Throwable cause) {
        super(message, cause);
    }

    public TCGlobalContextException(Throwable cause) {
        super(cause);
    }

    public TCGlobalContextException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
