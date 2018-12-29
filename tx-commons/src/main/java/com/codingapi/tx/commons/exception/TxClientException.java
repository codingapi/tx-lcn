package com.codingapi.tx.commons.exception;

import lombok.NoArgsConstructor;

/**
 * Description:
 * Date: 2018/12/11
 *
 * @author ujued
 */
@NoArgsConstructor
public class TxClientException extends Exception {
    public TxClientException(String message) {
        super(message);
    }

    public TxClientException(Throwable ex) {
        super(ex);
    }
}
