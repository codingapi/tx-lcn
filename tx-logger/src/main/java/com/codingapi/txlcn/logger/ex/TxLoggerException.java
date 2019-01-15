package com.codingapi.txlcn.logger.ex;

/**
 * Description:
 * Date: 19-1-15 下午1:21
 *
 * @author ujued
 */
public class TxLoggerException extends Exception {
    public TxLoggerException() {
    }

    public TxLoggerException(String message) {
        super(message);
    }

    public TxLoggerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TxLoggerException(Throwable cause) {
        super(cause);
    }

    public TxLoggerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
