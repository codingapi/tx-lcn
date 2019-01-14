package com.codingapi.txlcn.commons.exception;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/27
 *
 * @author codingapi
 */
public class BeforeBusinessException extends Exception {

    public BeforeBusinessException() {
    }

    public BeforeBusinessException(String message) {
        super(message);
    }

    public BeforeBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeforeBusinessException(Throwable cause) {
        super(cause);
    }

    public BeforeBusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
