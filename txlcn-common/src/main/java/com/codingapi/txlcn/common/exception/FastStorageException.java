package com.codingapi.txlcn.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description:
 * Date: 19-1-21 下午3:28
 *
 * @author ujued
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FastStorageException extends Exception {

    public static final int EX_CODE_REPEAT_GROUP = 100;

    public static final int EX_CODE_NON_GROUP = 101;

    public static final int EX_CODE_REPEAT_LOCK = 102;

    private int code;


    public FastStorageException(int code) {
        this.code = code;
    }

    public FastStorageException(String message, int code) {
        super(message);
        this.code = code;
    }

    public FastStorageException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public FastStorageException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public FastStorageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    @Override
    public String toString() {
        return super.toString() + "(code=101)";
    }
}
