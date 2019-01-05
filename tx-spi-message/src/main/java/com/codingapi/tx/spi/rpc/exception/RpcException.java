package com.codingapi.tx.spi.rpc.exception;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
public class RpcException extends Exception {

    public static final int NON_TX_MANAGER = 40010;

    private int code;

    public RpcException() {
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(int code, String message) {
        super(message);
        this.code = code;
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

    public RpcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
