package com.codingapi.tx.commons.exception;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/27
 *
 * @author codingapi
 */
public class JoinGroupException extends Exception {

    public JoinGroupException() {
    }

    public JoinGroupException(String message) {
        super(message);
    }

    public JoinGroupException(String message, Throwable cause) {
        super(message, cause);
    }

    public JoinGroupException(Throwable cause) {
        super(cause);
    }

    public JoinGroupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
